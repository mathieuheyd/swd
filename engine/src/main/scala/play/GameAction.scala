package play

import entities.DiceSideSymbol
import play.history._

import scala.collection.mutable

sealed trait GameAction {
  val phase: GamePhase.Value

  def isValid(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): Boolean
  def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): HistoryEvent
}

case class DrawStartingHand() extends GameAction {
  override val phase = GamePhase.Setup
  override def isValid(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): Boolean = {
    !history.setupActions.exists(event => event.action.isInstanceOf[DrawStartingHand] && event.player == player)
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): HistoryEvent = {
    val effects = mutable.Buffer.empty[HistoryEffect]

    playerArea.shuffleDeck()
    val cards = playerArea.drawCards(5)
    playerArea.putCardsInHand(cards)
    effects += DrawHandEffect(cards.map(_.uniqueId).toList)

    val event = HistoryEvent(player, this, effects)
    history.setupActions += event
    event
  }
}

case class MulliganAction(cards: List[Int]) extends GameAction {
  override val phase = GamePhase.Mulligan
  override def isValid(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): Boolean = {
    !history.setupActions.exists(event => event.action.isInstanceOf[MulliganAction] && event.player == player) &&
    cards.forall(id => playerArea.getCardInHand(id).isDefined)
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): HistoryEvent = {
    val effects = mutable.Buffer.empty[HistoryEffect]

    var drawnCards: Seq[InPlayCard] = Seq.empty
    if (cards.nonEmpty) {
      cards.map(id => playerArea.popCardFromHand(id)).foreach(c => playerArea.putCardInDeck(c.get))
      playerArea.shuffleDeck()
      drawnCards = playerArea.drawCards(cards.size)
      playerArea.putCardsInHand(drawnCards)
    }

    effects += MulliganEffect(cards, drawnCards.map(_.uniqueId).toList)

    val event = HistoryEvent(player, this, effects)
    history.setupActions += event
    event
  }
}

case class TossAction() extends GameAction {
  override val phase = GamePhase.Battlefield
  override def isValid(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): Boolean = {
    !history.setupActions.exists(event => event.action.isInstanceOf[TossAction] && event.player == player)
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): HistoryEvent = {
    val effects = mutable.Buffer.empty[HistoryEffect]

    val dices = playerArea.characters.flatMap(_.dices)
    dices.foreach { d => d.roll() }

    effects += TossEffect(dices.map(d => (d.uniqueId, d.sideId)).toList, dices.map(_.currentSide.value).sum)

    val event = HistoryEvent(player, this, effects)
    history.setupActions += event
    event
  }
}

case class ChooseBattlefield(card: Int) extends GameAction {
  override val phase = GamePhase.Battlefield
  override def isValid(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): Boolean = {
    Rule(
      history.setupActions.count(event => event.action.isInstanceOf[TossAction]) % 2 == 0,
      "Action must appear after toss has occured").isValid &&
    Rule(
      !history.setupActions.exists(event => event.action.isInstanceOf[ChooseBattlefield]),
      "Battlefield has already been chosen").isValid &&
    Rule(
      playerArea.characters.flatMap(_.dices.map(_.currentSide.value)).sum > opponentArea.characters.flatMap(_.dices.map(_.currentSide.value)).sum,
      "Wrong player choosing battlefield").isValid &&
    Rule(
      playerArea.battlefield.exists(_.uniqueId == card) || opponentArea.battlefield.exists(_.uniqueId == card),
      "Targeted card is not an in-game battlefield").isValid
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): HistoryEvent = {
    var battlefieldOwner = Player.Player1
    if (!playerArea.battlefield.exists(_.uniqueId == card)) {
      battlefieldOwner = player.opponent
      playerArea.battlefield = None
    }
    if (!opponentArea.battlefield.exists(_.uniqueId == card)) {
      battlefieldOwner = player
      opponentArea.battlefield = None
    }

    val effect = BattlefieldChosenEffect(battlefieldOwner)
    val event = HistoryEvent(player, this, Seq(effect))
    history.setupActions += event
    event
  }
}

case class AddShields(cards: List[Int]) extends GameAction {
  override val phase = GamePhase.Battlefield
  override def isValid(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): Boolean = {
    history.setupActions.exists(event => event.action.isInstanceOf[ChooseBattlefield]) &&
    playerArea.battlefield.isEmpty &&
    !history.setupActions.exists(event => event.action.isInstanceOf[AddShields]) &&
    cards.forall(playerArea.getCharacterOrSupport(_).isDefined)
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): HistoryEvent = {
    val shields = cards.groupBy(identity).mapValues(_.size)

    val effects = mutable.Buffer.empty[HistoryEffect]
    shields.foreach { case (character: Int, amount: Int) =>
      playerArea.getCharacterOrSupport(character).get.shields += amount
      effects += ShieldAddedEffect(character, amount)
    }

    val event = HistoryEvent(player, this, effects)
    history.setupActions += event
    event
  }
}

case class MulliganUpkeepAction(cardsToMulligan: List[Int]) extends GameAction {
  override val phase = GamePhase.Upkeep
  override def isValid(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): Boolean = {
    cardsToMulligan.forall(id => playerArea.getCardInHand(id).isDefined)
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): HistoryEvent = {
    val effects = mutable.Buffer.empty[HistoryEffect]

    if (cardsToMulligan.nonEmpty) {
      cardsToMulligan.map(id => playerArea.popCardFromHand(id)).foreach(
        c => {
          playerArea.putCardInDeck(c.get)
          effects += MulliganCardEffect(c.get.uniqueId)
        })
    }

    val cardsToDraw = 5 - playerArea.hand.size
    (0 to cardsToDraw).foreach { _ =>
      val drawCards = playerArea.drawCards(1)
      if (drawCards.size == 1) {
        playerArea.putCardsInHand(drawCards)
        drawCards.map(c => effects += DrawCardEffect(c.uniqueId))
      }
    }

    HistoryEvent(player, this, effects)
  }
}

case class PassAction() extends GameAction {
  override val phase = GamePhase.Action
  override def isValid(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): Boolean = {
    true
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): HistoryEvent = {
    HistoryEvent(player, this, Seq.empty)
  }
}

case class ActivateAction(card: Int) extends GameAction {
  override val phase = GamePhase.Action
  override def isValid(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): Boolean = {
    val character = playerArea.getCharacterOrSupport(card)
    return character.isDefined && !character.get.isActivated
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): HistoryEvent = {
    val effects = mutable.Buffer.empty[HistoryEffect]

    val character = playerArea.getCharacterOrSupport(card).get
    character.isActivated = true
    effects += CharacterActivatedEffect(character.uniqueId)

    character.dices.foreach(dice => {
      dice.inPool = true
      dice.roll()
      effects +=  DiceInPoolEffect(dice.uniqueId)
      effects +=  DiceRolledEffect(dice.uniqueId, dice.sideId)
    })

    HistoryEvent(player, this, effects)
  }
}

case class ResolveDices(dices: List[Int], targets: List[Int]) extends GameAction {
  override val phase = GamePhase.Action
  override def isValid(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): Boolean = {
    val inPlayDices = dices.map(id => playerArea.getDice(id))
    val areDefined = inPlayDices.forall(d => d.isDefined)
    lazy val areInPool = inPlayDices.forall(d => d.get.inPool)
    lazy val sameSymbol = inPlayDices.map(d => d.get.currentSide.symbol).toSet.size == 1
    lazy val atLeastOneNonModifier = inPlayDices.exists(d => !d.get.currentSide.modifier)
    lazy val enoughResources = inPlayDices.map(d => d.get.currentSide.cost).sum <= playerArea.resources
    lazy val validTargets = inPlayDices.head.get.currentSide.symbol match {
      case DiceSideSymbol.MeleeDamage
           | DiceSideSymbol.RangedDamage
           | DiceSideSymbol.Shield =>
        targets.size == 1 && opponentArea.getCharacterOrSupport(targets.head).isDefined
      case DiceSideSymbol.Focus =>
        targets.size == inPlayDices.map(d => d.get.currentSide.value).sum &&
        Set(targets).size == targets.size &&
        targets.forall(t => playerArea.getDice(t).exists(_.inPool))
      case DiceSideSymbol.Resource
           | DiceSideSymbol.Disrupt
           | DiceSideSymbol.Discard =>
        true
    }
    areDefined && areInPool && sameSymbol && atLeastOneNonModifier && enoughResources && validTargets
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): HistoryEvent = {
    val effects = mutable.Buffer.empty[HistoryEffect]

    val inPlayDices = dices.map(id => playerArea.getDice(id).get)

    val totalCost = inPlayDices.map(d => d.currentSide.cost).sum
    if (totalCost > 0) {
      playerArea.resources -= totalCost
      effects += ResourceRemovedEffect(Player.Player1, totalCost)
    }

    val totalValue = inPlayDices.map(d => d.currentSide.value).sum
    inPlayDices.head.currentSide.symbol match {
      case DiceSideSymbol.MeleeDamage | DiceSideSymbol.RangedDamage =>
        val target = opponentArea.getCharacterOrSupport(targets.head).get
        val shieldsRemoved = Math.min(target.shields, totalValue)
        val damageDealt = Math.min(totalValue - shieldsRemoved, target.health)
        if (shieldsRemoved > 0) {
          target.shields -= shieldsRemoved
          effects += ShieldRemovedEffect(target.uniqueId, shieldsRemoved)
        }
        if (damageDealt > 0) {
          target.health -= damageDealt
          effects += DamageDealtEffect(target.uniqueId, damageDealt)
        }
      case DiceSideSymbol.Resource =>
        playerArea.resources += totalValue
        effects += ResourceAddedEffect(playerArea.player, totalValue)
      case DiceSideSymbol.Shield =>
        val target = playerArea.getCharacterOrSupport(targets.head).get
        val shieldsAdded = Math.min(3 - target.shields, totalValue)
        effects += ShieldAddedEffect(target.uniqueId, shieldsAdded)
      case DiceSideSymbol.Disrupt =>
        val removedResources = Math.min(opponentArea.resources, totalValue)
        opponentArea.resources -= removedResources
        effects += ResourceRemovedEffect(opponentArea.player, removedResources)
      case DiceSideSymbol.Discard =>
        (1 to totalValue).foreach({ _ =>
          val card = opponentArea.getRandomCardFromHand()
          card match { case Some(c) =>
            opponentArea.popCardFromHand(c.uniqueId)
            opponentArea.putCardInDiscardPile(c)
            effects += CardDiscardedEffect(c.uniqueId)
          }
        })
      case DiceSideSymbol.Focus =>
        val dices = targets.map(playerArea.getDice(_).get)
        dices.foreach({ dice =>
          dice.roll()
          effects += DiceRolledEffect(dice.uniqueId, dice.sideId)
        })
    }

    dices.map(id => playerArea.getDice(id).get).foreach(dice => {
      effects += DiceOutPoolEffect(dice.uniqueId)
    })

    HistoryEvent(player, this, effects)
  }
}

case class DiscardReroll(card: Int, dice: Int) extends GameAction {
  override val phase = GamePhase.Action
  override def isValid(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): Boolean = {
    playerArea.getCardInHand(card).isDefined && playerArea.getDice(dice).exists(_.inPool)
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): HistoryEvent = {
    val effects = mutable.Buffer.empty[HistoryEffect]

    val c = playerArea.popCardFromHand(card).get
    playerArea.putCardInDiscardPile(c)
    effects += CardDiscardedEffect(c.uniqueId)

    val d = playerArea.getDice(dice).get
    d.roll()
    effects += DiceRolledEffect(d.uniqueId, d.sideId)

    HistoryEvent(player, this, effects)
  }
}

//case class PlayCard(card: Int) extends GameAction {
//  override def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
//    true
//  }
//  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent] = {
//    Seq(PassEvent(player))
//  }
//}
//
//case class CardAbility(card: Int) extends GameAction {
//  override def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
//    true
//  }
//  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent] = {
//    Seq(PassEvent(player))
//  }
//}

case class ClaimBattlefield() extends GameAction {
  override val phase = GamePhase.Action
  override def isValid(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): Boolean = {
    !playerArea.battlefieldClaimed && !opponentArea.battlefieldClaimed
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea, history: GameHistory): HistoryEvent = {
    val battlefield = Seq(playerArea.battlefield, opponentArea.battlefield).flatten.head
    playerArea.battlefield = Some(battlefield)
    HistoryEvent(player, this, Seq.empty)
  }
}

private case class Rule(check: Boolean, reason: String) {
  def isValid = {
    if (!check) {
      Console.out.println(reason)
    }
    check
  }
}
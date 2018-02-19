package play

import entities.DiceSideSymbol
import play.history._

import scala.collection.mutable

trait GameAction {
  val player: Player.Value

  def isValid(playerArea: PlayerArea, opponentArea: PlayerArea): Boolean
  def process(playerArea: PlayerArea, opponentArea: PlayerArea): HistoryEvent
}

//case class MulliganAction(cards: Seq[Int]) extends GameAction {
//  override def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
//    phase == GamePhase.Mulligan && cards.forall(id => !playerArea.getCardInHand(id).isEmpty)
//  }
//
//  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent] = {
//    var drawEvent: Option[DrawEvent] = None
//    if (cards.nonEmpty) {
//      val c = cards.map(id => playerArea.popCardFromHand(id)).foreach(c => playerArea.putCardInDeck(c.get))
//      playerArea.shuffleDeck()
//      val drawCards = playerArea.drawCards(cards.size)
//      playerArea.putCardsInHand(drawCards)
//      drawEvent = Some(DrawEvent(player, drawCards.map(_.uniqueId)))
//    }
//
//    Seq(MulliganEvent(player, cards)) ++ drawEvent
//  }
//}

//case class PassAction() extends GameAction {
//  override def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
//    true
//  }
//  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent] = {
//    Seq(PassEvent(player))
//  }
//}

case class ActivateAction(player: Player.Value, card: Int) extends GameAction {
  override def isValid(playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
    val character = playerArea.getCharacterOrSupport(card)
    return character.isDefined && !character.get.isActivated
  }
  override def process(playerArea: PlayerArea, opponentArea: PlayerArea): HistoryEvent = {
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

    HistoryEvent(this, effects)
  }
}

case class ResolveDices(player: Player.Value, dices: Seq[Int], targets: Seq[Int]) extends GameAction {
  override def isValid(playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
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
  override def process(playerArea: PlayerArea, opponentArea: PlayerArea): HistoryEvent = {
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
          effects += ShiedRemovedEffect(target.uniqueId, shieldsRemoved)
        }
        if (damageDealt > 0) {
          target.health -= damageDealt
          effects += DamageDealtEffect(target.uniqueId, damageDealt)
        }
      case DiceSideSymbol.Resource =>
        playerArea.resources += totalValue
        effects += ResourceAddedEffect(playerArea.player, totalValue)
      case DiceSideSymbol.Shield =>
        val target = opponentArea.getCharacterOrSupport(targets.head).get
        val shieldsAdded = Math.min(3 - target.shields, totalValue)
        effects += ShiedAddedEffect(target.uniqueId, shieldsAdded)
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

    HistoryEvent(this, effects)
  }
}

case class DiscardReroll(player: Player.Value, card: Int, dice: Int) extends GameAction {
  override def isValid(playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
    playerArea.getCardInHand(card).isDefined && playerArea.getDice(dice).exists(_.inPool)
  }
  override def process(playerArea: PlayerArea, opponentArea: PlayerArea): HistoryEvent = {
    val effects = mutable.Buffer.empty[HistoryEffect]

    val c = playerArea.popCardFromHand(card).get
    playerArea.putCardInDiscardPile(c)
    effects += CardDiscardedEffect(c.uniqueId)

    val d = playerArea.getDice(dice).get
    d.roll()
    effects += DiceRolledEffect(d.uniqueId, d.sideId)

    HistoryEvent(this, effects)
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
//
//case class ClaimBattlefield() extends GameAction {
//  override def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
//    true
//  }
//  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent] = {
//    Seq(PassEvent(player))
//  }
//}
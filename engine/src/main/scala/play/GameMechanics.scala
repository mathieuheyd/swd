package play

import entities.{CardId, FullDeck}
import play.GamePhase.GamePhase
import play.history._

import scala.collection.mutable

object Player extends Enumeration {
  type Player = Value
  val Player1, Player2 = Value

  class PlayerValue(player: Value) {
    def opponent: Player.Value = {
      if (player == Player1) Player2 else Player1
    }
  }

  implicit def value2PlayerValue(player: Player) = new PlayerValue(player)
}

// Games phases:
// Setup
// Round[]
//  - Mulligan
//  - Gain 2 resources
//  - if battlefield not claimed, assign it
//  - Actions until both players pass
object GamePhase extends Enumeration {
  type GamePhase = Value
  val Setup, Mulligan, Battlefield, AddShields, Action, Upkeep = Value
}

case class UniqueIdGenerator(startId: Int) {
  var currentId = startId
  def next() = {
    val id = currentId
    currentId += 1
    id
  }
}

object ActionType extends Enumeration {
  type ActionType = Value
  val Mulligan = Value
  val ChooseBattlefield = Value
  val AddShields = Value
  val Action = Value
}

case class ActionRequired(player: Player.Value, actionType: ActionType.Value)

case class GameEvent(events: Seq[HistoryEvent], nextActions: Seq[ActionRequired])

class GameMechanics(val deckPlayer1: FullDeck, val deckPlayer2: FullDeck) {

  val gameHistory: GameHistory = new GameHistory
  var cardIds: mutable.Map[Int, CardId] = mutable.Map.empty

  var phase: GamePhase = GamePhase.Setup
  var currentPlayer: Player.Value = Player.Player1

  val areaPlayer1: PlayerArea = initPlayerArea(Player.Player1, deckPlayer1, 100)
  val areaPlayer2: PlayerArea = initPlayerArea(Player.Player2, deckPlayer2, 200)

  def initPlayerArea(player: Player.Value, deck: FullDeck, startId: Int): PlayerArea = {
    val id = UniqueIdGenerator(startId)
    val inPlayCharacters = deck.characters.zipWithIndex.map { c =>
      val characterId = id.next
      val firstDice = new InPlayDice(id.next, c._1.character.dice.get)
      val dices = if (c._1.elite) Seq(firstDice, new InPlayDice(id.next, c._1.character.dice.get)) else Seq(firstDice)

      cardIds += (characterId -> c._1.character.id)
      dices.foreach(dice => cardIds += (dice.uniqueId -> c._1.character.id))

      new InPlayCharacter(characterId, c._1.character, dices)
    }

    val cardsWithId = deck.deck.zipWithIndex.map { c =>
      val cardId = id.next
      val dice = c._1.dice.map { d => new InPlayDice(id.next, d) }

      cardIds += (cardId -> c._1.id)
      dice.foreach(dice => cardIds += (dice.uniqueId -> c._1.id))

      InPlayCard(cardId, c._1, dice)
    }

    val battlefield = InPlayBattlefield(id.next, deck.battlefield)
    cardIds += (battlefield.uniqueId -> battlefield.card.id)

    new PlayerArea(player, inPlayCharacters, new Deck(cardsWithId), Some(battlefield))
  }

  def drawStartingHands(): GameEvent = {
    val events1 = handleAction(Player.Player1, DrawStartingHand())
    val events2 = handleAction(Player.Player2, DrawStartingHand())

    phase = GamePhase.Mulligan

    val events = Seq(events1.events, events2.events).flatten
    val nextActions = Seq(
      ActionRequired(Player.Player1, ActionType.Mulligan),
      ActionRequired(Player.Player2, ActionType.Mulligan)
    )

    GameEvent(events, nextActions)
  }

  def handleSetup(): GameEvent = {
    val eventSetup1 = handleAction(Player.Player1, SetupAction())
    val eventSetup2 = handleAction(Player.Player2, SetupAction())

    val eventToss1 = handleAction(Player.Player1, TossAction())
    val eventToss2 = handleAction(Player.Player2, TossAction())

    val total1 = eventToss1.events.head.effects.head.asInstanceOf[TossEffect].total
    val total2 = eventToss2.events.head.effects.head.asInstanceOf[TossEffect].total
    val winner = if (total1 >= total2) Player.Player1 else Player.Player2

    val events = Seq(eventSetup1.events, eventSetup2.events, eventToss1.events, eventToss2.events).flatten
    val nextAction = ActionRequired(winner, ActionType.ChooseBattlefield)

    GameEvent(events, Seq(nextAction))
  }

  def handleAction(player: Player.Value, action: GameAction): GameEvent = {
    var events = mutable.Buffer.empty[HistoryEvent]
    var actions = mutable.Buffer.empty[ActionRequired]

    val (playerArea, opponentArea) = if (player == Player.Player1) (areaPlayer1, areaPlayer2) else (areaPlayer2, areaPlayer1)

    val validAction =
      Rule(action.phase == phase, "Action must appear in correct phase").isValid &&
      Rule(phase != GamePhase.Action || player == currentPlayer, "Not current player turn").isValid &&
      action.isValid(player, playerArea, opponentArea, gameHistory)

    if (!validAction) {
      Console.out.println("Invalid action", player, action)
      return null
    }

    events += action.process(player, playerArea, opponentArea, gameHistory)

    // Post action
    phase match {
      case GamePhase.Mulligan =>
        if (gameHistory.setupActions.count(event => event.action.isInstanceOf[MulliganAction]) == 2) {
          phase = GamePhase.Battlefield
          val tossOutput = handleSetup()
          events ++= tossOutput.events
          actions ++= tossOutput.nextActions
        }
      case GamePhase.Battlefield =>
        if (gameHistory.setupActions.exists(event => event.action.isInstanceOf[ChooseBattlefield])) {
          phase = GamePhase.AddShields
          val battlefieldLoser = if (areaPlayer1.battlefield.isEmpty) Player.Player1 else Player.Player2
          actions :+= ActionRequired(battlefieldLoser, ActionType.AddShields)
        }
      case GamePhase.AddShields =>
        val battlefieldOwner = if (areaPlayer1.battlefield.isDefined) Player.Player1 else Player.Player2
        if (gameHistory.setupActions.count(event => event.action.isInstanceOf[AddShield]) < 3) {
          actions :+= ActionRequired(battlefieldOwner.opponent, ActionType.AddShields)
        } else {
          phase = GamePhase.Action
          currentPlayer = battlefieldOwner
          actions :+= ActionRequired(battlefieldOwner, ActionType.Action)
        }
      case GamePhase.Action => {
        val bothPlayersPass = gameHistory.currentRoundActions
          .takeRight(2)
          .count(_.action.isInstanceOf[PassAction]) == 2
        if (bothPlayersPass) {
          phase = GamePhase.Upkeep
          upkeepPhase()
        } else {
          if (opponentArea.battlefieldClaimed) {
            val automaticPassAction = PassAction()
            handleAction(player.opponent, automaticPassAction)
          } else {
            currentPlayer = currentPlayer.opponent
          }
          actions :+= ActionRequired(currentPlayer, ActionType.Action)
        }
      }
      case _ =>
    }

    GameEvent(events, actions)
  }

  def upkeepPhase(): Unit = {
    val effects = mutable.Buffer.empty[HistoryEffect]

    areaPlayer1.characters.foreach(character => {
      if (character.health > 0 && character.isActivated) {
        character.isActivated = false
        effects += CharacterReadiedEffect(character.uniqueId)

        character.dices.map(dice => {
          if (dice.inPool) {
            dice.inPool = false
            effects += DiceOutPoolEffect(dice.uniqueId)
          }
        })
      }
    })

    areaPlayer1.resources += 2
    effects += ResourceAddedEffect(Player.Player1, 2)

    areaPlayer2.resources += 2
    effects += ResourceAddedEffect(Player.Player2, 2)
  }

}

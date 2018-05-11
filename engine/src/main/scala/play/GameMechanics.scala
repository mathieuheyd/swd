package play

import entities.FullDeck
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
  val Setup, Mulligan, Battlefield, Action, Upkeep = Value
}

class GameMechanics(val deckPlayer1: FullDeck, val deckPlayer2: FullDeck) {

  var phase: GamePhase = GamePhase.Setup
  var currentPlayer: Player.Value = Player.Player1

  val areaPlayer1: PlayerArea = initPlayerArea(Player.Player1, deckPlayer1, 100)
  val areaPlayer2: PlayerArea = initPlayerArea(Player.Player2, deckPlayer2, 200)

  val gameHistory: GameHistory = new GameHistory

  var currentRoundHistory = HistoryRound(Seq.empty, Seq.empty, Seq.empty)

  def initPlayerArea(player: Player.Value, deck: FullDeck, startId: Int): PlayerArea = {
    val inPlayCharacters = deck.characters.zipWithIndex.map { c =>
      val uniqueId = startId + c._2 * 3
      val firstDice = new InPlayDice(uniqueId + 1, c._1.character.dice.get)
      val dices = if (c._1.elite) Array(firstDice, new InPlayDice(uniqueId + 2, c._1.character.dice.get)) else Array(firstDice)
      new InPlayCharacter(uniqueId, c._1.character, dices)
    }
    val cardsWithId = deck.deck.zipWithIndex.map { c =>
      val uniqueId = startId + deck.characters.length * 3 + c._2 * 2
      val dice = c._1.dice.map { d => new InPlayDice(uniqueId = 1, d) }
      InPlayCard(uniqueId, c._1, dice)
    }
    new PlayerArea(player, inPlayCharacters, new Deck(cardsWithId))
  }

  def drawStartingHands(): Seq[HistoryEvent] = {
    val events1 = handleAction(Player.Player1, DrawStartingHand())
    val events2 = handleAction(Player.Player2, DrawStartingHand())

    phase = GamePhase.Mulligan

    Seq(events1, events2).flatten
  }

  def handleToss(): Seq[HistoryEvent] = {
    val events1 = handleAction(Player.Player1, TossAction())
    val events2 = handleAction(Player.Player2, TossAction())

    Seq(events1, events2).flatten
  }

  def handleAction(player: Player.Value, action: GameAction): Seq[HistoryEvent] = {
    val (playerArea, opponentArea) = if (player == Player.Player1) (areaPlayer1, areaPlayer2) else (areaPlayer2, areaPlayer1)

    val validAction = action.phase == phase &&
      (phase match {
        case GamePhase.Action => player == currentPlayer
        case _ => true
      }) &&
      action.isValid(player, playerArea, opponentArea, gameHistory)

    if (!validAction) return Seq.empty

    val event = action.process(player, playerArea, opponentArea, gameHistory)
    currentRoundHistory = HistoryRound(currentRoundHistory.actions :+ HistoryTurn(Seq(event)), Seq.empty, Seq.empty)

    // Post action
    phase match {
      case GamePhase.Action => {
        val bothPlayersPass = currentRoundHistory.actions
          .takeRight(2)
          .forall(turn => turn.events.headOption.map(_.action) match {
            case Some(PassAction()) => true
            case _ => false
          })
        if (bothPlayersPass) {
          phase = GamePhase.Upkeep
          upkeepPhase()
        } else {
          // end of the turn
          if (opponentArea.battlefieldClaimed) {
            val automaticPassAction = PassAction()
            val passEvent = automaticPassAction.process(player.opponent, opponentArea, playerArea, gameHistory)
            currentRoundHistory = HistoryRound(currentRoundHistory.actions :+ HistoryTurn(Seq(passEvent)), Seq.empty, Seq.empty)
          } else {
            currentPlayer = currentPlayer.opponent
          }
        }
      }
      case _ =>
    }

    Seq(event)
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

    currentRoundHistory = HistoryRound(currentRoundHistory.actions, effects, Seq.empty)
  }

}

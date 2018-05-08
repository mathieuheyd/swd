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

  def drawStartingHands() = {
    areaPlayer1.shuffleDeck()
    areaPlayer2.shuffleDeck()

    val cardsPlayer1 = areaPlayer1.drawCards(5)
    areaPlayer1.putCardsInHand(cardsPlayer1)

    val cardsPlayer2 = areaPlayer2.drawCards(5)
    areaPlayer2.putCardsInHand(cardsPlayer2)

    phase = GamePhase.Mulligan
  }

  def handleAction(player: Player.Value, action: GameAction): Option[HistoryEvent] = {
    val (playerArea, opponentArea) = if (player == Player.Player1) (areaPlayer1, areaPlayer2) else (areaPlayer2, areaPlayer1)

    val validAction = action.phase == phase &&
      (phase match {
        case GamePhase.Action => player == currentPlayer
        case _ => true
      }) &&
      action.isValid(player, playerArea, opponentArea, gameHistory)

    if (!validAction) return None

    val event = action.process(player, playerArea, opponentArea)
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
            val passEvent = automaticPassAction.process(player.opponent, opponentArea, playerArea)
            currentRoundHistory = HistoryRound(currentRoundHistory.actions :+ HistoryTurn(Seq(passEvent)), Seq.empty, Seq.empty)
          } else {
            currentPlayer = currentPlayer.opponent
          }
        }
      }
      case _ =>
    }

    Some(event)
  }

  def rollBattlefieldDecider(): Player.Value = {
    val dices1 = areaPlayer1.characters.flatMap(_.dices)
    dices1.foreach { d => d.roll() }
    val sum1 = dices1.map { _.currentSide.value }.sum

    val dices2 = areaPlayer2.characters.flatMap(_.dices)
    dices2.foreach { d => d.roll() }
    val sum2 = dices1.map { _.currentSide.value }.sum

    if (sum1 == sum2) return rollBattlefieldDecider()

    if (sum1 > sum2) Player.Player1 else Player.Player2
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

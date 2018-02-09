package play

import entities.FullDeck
import play.GamePhase.GamePhase

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
  val Setup, Mulligan, Battlefield, Action = Value
}

class GameMechanics(deckPlayer1: FullDeck, deckPlayer2: FullDeck) {

  var phase: GamePhase = GamePhase.Setup
  var currentPlayer: Player.Value = Player.Player1
  var eventsHistory: Seq[GameEvent] = Seq.empty

  val areaPlayer1: PlayerArea = initPlayerArea(deckPlayer1, 100)
  val areaPlayer2: PlayerArea = initPlayerArea(deckPlayer2, 200)

  def addEvent(event: GameEvent) = {
    eventsHistory = eventsHistory :+ event
  }

  def initPlayerArea(deck: FullDeck, startId: Int): PlayerArea = {
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
    new PlayerArea(inPlayCharacters, new Deck(cardsWithId))
  }

  def drawStartingHands() = {
    areaPlayer1.shuffleDeck()
    areaPlayer2.shuffleDeck()

    val cardsPlayer1 = areaPlayer1.drawCards(5)
    areaPlayer1.putCardsInHand(cardsPlayer1)
    addEvent(DrawEvent(Player.Player1, cardsPlayer1.map(_.uniqueId)))

    val cardsPlayer2 = areaPlayer2.drawCards(5)
    areaPlayer2.putCardsInHand(cardsPlayer2)
    addEvent(DrawEvent(Player.Player2, cardsPlayer2.map(_.uniqueId)))

    phase = GamePhase.Mulligan
  }

  def handleAction(player: Player.Value, action: GameAction) = {
    val (playerArea, opponentArea) = if (player == Player.Player1) (areaPlayer1, areaPlayer2) else (areaPlayer2, areaPlayer1)
    if (action.isValid(phase, playerArea, opponentArea)) {
      val events = action.process(player, playerArea, opponentArea)
      eventsHistory = eventsHistory ++ events
    }

    // Post action
    phase match {
      case GamePhase.Mulligan => {
        if (eventsHistory.count(event => event.isInstanceOf[MulliganEvent]) == 2) {
          phase = GamePhase.Battlefield
        }
      }
      case GamePhase.Battlefield => None
      case GamePhase.Action => None
    }
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

  def selectBattlefield(playerBattlefield: Player.Value) = {

  }

  def addShields(character1: Int, character2: Int, character3: Int) = {

  }

  def startNewRound() = {
    //areaPlayer1.gainResources(2)
    //areaPlayer2.gainResources(2)


  }

}

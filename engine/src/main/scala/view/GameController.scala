package view

import entities.{Battlefield, CardId}
import play._

import scala.collection.mutable

case class GameState(playerArea: PlayerAreaState, playerArea2: OpponentAreaState)
case class PlayerAreaState(characterState: List[CharacterState], hand: List[CardId], cardsInDeck: Int)
case class OpponentAreaState(characterState: List[CharacterState], cardsInHand: Int, cardsInDeck: Int)
case class CharacterState(card: CardId, damages: Int, shields: Int, activated: Boolean, dices: List[DiceState])
case class DiceState(inPool: Boolean, side: Int)

case class CharacterView(uniqueId: Int, card: CardId, dices: List[DiceView])
case class DiceView(uniqueId: Int, card: CardId)
case class CardView(uniqueId: Int, card: CardId)
case class PlayerSetupView(characters: List[CharacterView], battlefield: CardId, deckSize: Int)

sealed trait EventView
case class SetupView(player: PlayerSetupView, opponent: PlayerSetupView) extends EventView
case class DrawStartingHandView(player: List[CardView], opponent: Int) extends EventView

case class ActionView() extends EventView
case class EffectView() extends EventView

case class BothSideEventView(player1: EventView, player2: EventView)

class GameController(gameMechanics: GameMechanics) {

  def startGame(): Seq[BothSideEventView] = {
    var events = mutable.Buffer.empty[BothSideEventView]

    // Setup board
    val setupPlayer1 = playerSetupView(gameMechanics.areaPlayer1, gameMechanics.deckPlayer1.battlefield)
    val setupPlayer2 = playerSetupView(gameMechanics.areaPlayer2, gameMechanics.deckPlayer2.battlefield)
    events += BothSideEventView(
      SetupView(setupPlayer1, setupPlayer2),
      SetupView(setupPlayer2, setupPlayer1)
    )

    // Draw starting hands
    gameMechanics.drawStartingHands()
    val handPlayer1 = startingHandView(gameMechanics.areaPlayer1)
    val handPlayer2 = startingHandView(gameMechanics.areaPlayer2)
    events += BothSideEventView(
      DrawStartingHandView(handPlayer1, handPlayer2.size),
      DrawStartingHandView(handPlayer2, handPlayer1.size)
    )

    events
  }

  private def playerSetupView(areaPlayer: PlayerArea, battlefield: Battlefield) = {
    PlayerSetupView(
      areaPlayer.characters.map { c =>
        CharacterView(
          c.uniqueId,
          c.character.id,
          c.dices.map(d => DiceView(d.uniqueId, c.character.id)).toList)
      }.toList,
      battlefield.id,
      areaPlayer.deck.cards.size)
  }

  private def startingHandView(areaPlayer: PlayerArea) = {
    areaPlayer.hand.map(c => CardView(c.uniqueId, c.card.id)).toList
  }

  def playerAction(player: Player.Value, action: GameAction): Seq[BothSideEventView] = {
    gameMechanics.handleAction(player, action)

    Seq.empty
  }

  def getFullState(player: Player.Value): GameState = {
    val (playerArea, opponentArea) = player match {
      case Player.Player1 => (gameMechanics.areaPlayer1, gameMechanics.areaPlayer2)
      case Player.Player2 => (gameMechanics.areaPlayer2, gameMechanics.areaPlayer1)
    }

    GameState(
      PlayerAreaState(
        playerArea.characters.map(
          c => CharacterState(c.character.id, c.shields, c.character.maxHealth - c.health, c.isActivated, c.dices.map(
            d => DiceState(d.inPool, d.sideId)
          ).toList)
        ).toList,
        playerArea.hand.map(c => c.card.id).toList,
        playerArea.deck.cards.size),
      OpponentAreaState(
        opponentArea.characters.map(
          c => CharacterState(c.character.id, c.shields, c.character.maxHealth - c.health, c.isActivated, c.dices.map(
            d => DiceState(d.inPool, d.sideId)
          ).toList)
        ).toList,
        opponentArea.hand.size,
        opponentArea.deck.cards.size
      )
    )
  }

}

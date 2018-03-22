package play

import entities.CardId

case class GameState(playerArea: PlayerAreaState, playerArea2: OpponentAreaState)
case class PlayerAreaState(characterState: Seq[CharacterState], hand: Seq[CardId], cardsInDeck: Int)
case class OpponentAreaState(characterState: Seq[CharacterState], cardsInHand: Int, cardsInDeck: Int)
case class CharacterState(card: CardId, damages: Int, shields: Int, activated: Boolean, dices: Seq[DiceState])
case class DiceState(card: CardId, inPool: Boolean, side: Int)

class GameView(gameMechanics: GameMechanics) {

  def getFullState(player: Player.Value): GameState = {
    val (playerArea, opponentArea) = player match {
      case Player.Player1 => (gameMechanics.areaPlayer1, gameMechanics.areaPlayer2)
      case Player.Player2 => (gameMechanics.areaPlayer2, gameMechanics.areaPlayer1)
    }

    GameState(
      PlayerAreaState(
        playerArea.characters.map(
          c => CharacterState(c.character.id, c.shields, c.character.maxHealth - c.health, c.isActivated, c.dices.map(
            d => DiceState(c.character.id, d.inPool, d.sideId)
          ))
        ),
        playerArea.hand.map(c => c.card.id),
        playerArea.deck.cards.size),
      OpponentAreaState(
        opponentArea.characters.map(
          c => CharacterState(c.character.id, c.shields, c.character.maxHealth - c.health, c.isActivated, c.dices.map(
            d => DiceState(c.character.id, d.inPool, d.sideId)
          ))
        ),
        opponentArea.hand.size,
        opponentArea.deck.cards.size
      )
    )
  }

}

package view

import entities.FullDeck
import play._
import play.history._

import scala.collection.mutable

case class PlayerInfo(name: String, deck: FullDeck)
case class BothSideEventView(player1: EventView, player2: EventView)

class GameController(player1: PlayerInfo, player2: PlayerInfo) {

  val gameMechanics = new GameMechanics(player1.deck, player2.deck)

  // Could be cleaner in building SetupView and DrawStartingHandView
  def startGame(): Seq[BothSideEventView] = {
    var events = mutable.Buffer.empty[BothSideEventView]

    val playerInfo1 = PlayerInfoView(player1.name)
    val playerInfo2 = PlayerInfoView(player2.name)
    events += BothSideEventView(
      GameInfoView(playerInfo1, playerInfo2),
      GameInfoView(playerInfo2, playerInfo1)
    )

    // Setup board
    val setupPlayer1 = playerSetupView(gameMechanics.areaPlayer1)
    val setupPlayer2 = playerSetupView(gameMechanics.areaPlayer2)
    events += BothSideEventView(
      SetupView(setupPlayer1, setupPlayer2),
      SetupView(setupPlayer2, setupPlayer1)
    )

    // Draw starting hands
    val drawEvents = gameMechanics.drawStartingHands()
    events ++= drawEvents.events.map(buildBothSideView)
    events ++= drawEvents.nextActions.map(buildBothSideView)

    events
  }

  private def playerSetupView(areaPlayer: PlayerArea) = {
    PlayerSetupView(
      areaPlayer.characters.map { c =>
        CharacterView(
          c.uniqueId,
          c.character.id,
          c.dices.map(d => DiceView(d.uniqueId, c.character.id)).toList)
      }.toList,
      CardView(areaPlayer.battlefield.get.uniqueId, areaPlayer.battlefield.get.card.id),
      areaPlayer.deck.cards.size)
  }

  def playerAction(player: Player.Value, action: GameAction): Seq[BothSideEventView] = {
    val gameEvent = gameMechanics.handleAction(player, action)
    gameEvent.events.map(event => buildBothSideView(event)) ++
      gameEvent.nextActions.map(action => buildBothSideView(action))
  }

  private def buildBothSideView(event: HistoryEvent): BothSideEventView = {
    BothSideEventView(
      ActionView(
        event.player == Player.Player1,
        event.action,
        event.effects.map(effect => buildEffectView(effect, Player.Player1, event.player == Player.Player1)).toList),
      ActionView(
        event.player == Player.Player2,
        event.action,
        event.effects.map(effect => buildEffectView(effect, Player.Player2, event.player == Player.Player2)).toList)
    )
  }

  private def buildEffectView(effect: HistoryEffect, player: Player.Value, isPlayerAction: Boolean): EffectView = {
    effect match {
      case DrawHandEffect(cards: List[Int]) => if (isPlayerAction)
        DrawStartingHandView(cards.map(buildCardView)) else
        DrawStartingHandOpponentView(cards.size)
      case MulliganEffect(mulliganCards, drawnCards) => if (isPlayerAction)
        MulliganView(mulliganCards.map(buildCardView), drawnCards.map(buildCardView)) else
        MulliganOpponentView(mulliganCards.size, drawnCards.size)
      case TossEffect(dices, total) => TossView(dices.map(d => DiceRollView(buildDiceView(d._1), d._2)), total)
      case BattlefieldChosenEffect(battlefieldOwner) => ChooseBattlefieldView(player == battlefieldOwner)
      case ShieldAddedEffect(character, amount) => ShieldAddedView(buildCardView(character), amount)
      case CharacterActivatedEffect(character) => CharacterActivatedView(buildCardView(character))
      case DiceInPoolEffect(dice, side) => DiceInPoolView(buildDiceView(dice), side)
      case BattlefieldClaimedEffect() => BattlefieldClaimedView(isPlayerAction)
    }
  }

  private def buildBothSideView(action: ActionRequired): BothSideEventView = {
    BothSideEventView(
      ActionRequiredView(action.player == Player.Player1, action.actionType),
      ActionRequiredView(action.player == Player.Player2, action.actionType)
    )
  }

  private def buildCardView(uniqueId: Int): CardView = {
    CardView(uniqueId, gameMechanics.cardIds(uniqueId))
  }

  private def buildDiceView(uniqueId: Int): DiceView = {
    DiceView(uniqueId, gameMechanics.cardIds(uniqueId))
  }

  /*
  case class GameState(playerArea: PlayerAreaState, playerArea2: OpponentAreaState)
  case class PlayerAreaState(characterState: List[CharacterState], hand: List[CardId], cardsInDeck: Int)
  case class OpponentAreaState(characterState: List[CharacterState], cardsInHand: Int, cardsInDeck: Int)
  case class CharacterState(card: CardId, damages: Int, shields: Int, activated: Boolean, dices: List[DiceState])
  case class DiceState(inPool: Boolean, side: Int)

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
  */

}

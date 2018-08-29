package view

import entities.CardId
import play.{ActionType, GameAction}

sealed trait EventView {}
case class GameInfoView(player: PlayerInfoView, opponent: PlayerInfoView) extends EventView
case class SetupView(player: PlayerSetupView, opponent: PlayerSetupView) extends EventView
case class ActionView(player: Boolean, action: GameAction, effects: List[EffectView]) extends EventView
case class ActionRequiredView(player: Boolean, action: ActionType.Value) extends EventView

case class PlayerInfoView(name: String)
case class CharacterView(uniqueId: Int, card: CardId, dices: List[DiceView])
case class DiceView(uniqueId: Int, card: CardId)
case class CardView(uniqueId: Int, card: CardId)
case class PlayerSetupView(characters: List[CharacterView], battlefield: CardView, deckSize: Int)
case class DiceRollView(dice: DiceView, side: Int)

sealed trait EffectView
case class DrawStartingHandView(cards: List[CardView]) extends EffectView
case class DrawStartingHandOpponentView(cards: Int) extends EffectView
case class MulliganView(mulliganCards: List[CardView], drawnCards: List[CardView]) extends EffectView
case class MulliganOpponentView(mulliganCards: Int, drawnCards: Int) extends EffectView
case class TossView(dices: List[DiceRollView], total: Int) extends EffectView
case class ChooseBattlefieldView(player: Boolean) extends EffectView
case class ShieldAddedView(character: CardView, amount: Int) extends EffectView

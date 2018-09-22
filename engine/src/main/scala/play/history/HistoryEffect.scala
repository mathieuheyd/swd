package play.history

import play.Player

sealed trait HistoryEffect

case class DrawHandEffect(cards: List[Int]) extends HistoryEffect
case class MulliganEffect(mulliganCards: List[Int], drawnCards: List[Int]) extends HistoryEffect
case class TossEffect(dices: List[(Int, Int)], total: Int) extends HistoryEffect
case class BattlefieldChosenEffect(player: Player.Value) extends HistoryEffect

case class CharacterActivatedEffect(uniqueId: Int) extends HistoryEffect
case class CharacterReadiedEffect(uniqueId: Int) extends HistoryEffect
case class UpgradeAddedEffect(uniqueId: Int, characterId: Int) extends HistoryEffect
case class UpgradeDiscardedEffect(uniqueId: Int) extends HistoryEffect
case class DiceAddedEffect(uniqueId: Int, characterId: Int) extends HistoryEffect
case class DiceRolledEffect(uniqueId: Int, sideId: Int) extends HistoryEffect
case class DiceInPoolEffect(uniqueId: Int, sideId: Int) extends HistoryEffect
case class DiceOutPoolEffect(uniqueId: Int) extends HistoryEffect
case class DamageDealtEffect(uniqueId: Int, amount: Int) extends HistoryEffect
case class ShieldAddedEffect(uniqueId: Int, amount: Int) extends HistoryEffect
case class ShieldRemovedEffect(uniqueId: Int, amount: Int) extends HistoryEffect
case class ResourceAddedEffect(player: Player.Value, amount: Int) extends HistoryEffect
case class ResourceRemovedEffect(player: Player.Value, amount: Int) extends HistoryEffect
case class CardDiscardedEffect(uniqueId: Int) extends HistoryEffect
case class MulliganCardEffect(uniqueId: Int) extends HistoryEffect
case class DrawCardEffect(uniqueId: Int) extends HistoryEffect
case class BattlefieldClaimedEffect() extends HistoryEffect

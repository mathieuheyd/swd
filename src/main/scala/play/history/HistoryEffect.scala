package play.history

import play.Player

trait HistoryEffect {}

case class CharacterActivatedEffect(uniqueId: Int) extends HistoryEffect
case class CharacterReadiedEffect(uniqueId: Int) extends HistoryEffect
case class DiceRolledEffect(uniqueId: Int, sideId: Int) extends HistoryEffect
case class DiceInPoolEffect(uniqueId: Int) extends HistoryEffect
case class DiceOutPoolEffect(uniqueId: Int) extends HistoryEffect
case class DamageDealtEffect(uniqueId: Int, amount: Int) extends HistoryEffect
case class ShiedAddedEffect(uniqueId: Int, amount: Int) extends HistoryEffect
case class ShiedRemovedEffect(uniqueId: Int, amount: Int) extends HistoryEffect
case class ResourceAddedEffect(player: Player.Value, amount: Int) extends HistoryEffect
case class ResourceRemovedEffect(player: Player.Value, amount: Int) extends HistoryEffect
case class CardDiscardedEffect(uniqueId: Int) extends HistoryEffect

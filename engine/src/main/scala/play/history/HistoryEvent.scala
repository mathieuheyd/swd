package play.history

import play.{GameAction, Player}

case class HistoryEvent(player: Player.Value, action: GameAction, effects: Seq[HistoryEffect]) {}

package play.history

import play.GameAction

case class HistoryEvent(action: GameAction, effects: Seq[HistoryEffect]) {}

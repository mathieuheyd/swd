package play.history

import scala.collection.mutable

class GameHistory() {
  var setupActions: mutable.Buffer[HistoryEvent] = mutable.Buffer.empty
  var currentRoundActions: mutable.Buffer[HistoryEvent] = mutable.Buffer.empty
}

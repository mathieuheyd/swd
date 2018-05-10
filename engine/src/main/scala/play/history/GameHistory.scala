package play.history

import scala.collection.mutable

class GameHistory() {
  var mulliganActions: mutable.Buffer[HistoryEvent] = mutable.Buffer.empty
}

package play.history

case class HistoryRound(actions: Seq[HistoryTurn],
                        upkeep: Seq[HistoryEffect],
                        mulligans: Seq[HistoryEvent])

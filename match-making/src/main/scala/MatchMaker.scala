import scala.collection.mutable.ListBuffer

class MatchMaker {

  var registeredPlayers = ListBuffer[String]()

  def registerPlayer(id: String) = {
    registeredPlayers += id
  }

  def unregisterPlayer(id: String) = {
    registeredPlayers -= id
  }

  def newGame(): Option[(String, String)] = {
    if (registeredPlayers.size >= 2) {
      val player1 = registeredPlayers.remove(0)
      val player2 = registeredPlayers.remove(0)
      Some((player1, player2))
    } else
      None
  }

}

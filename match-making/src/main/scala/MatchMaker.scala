package main.scala

class MatchMaker {

  val registeredPlayers = Seq()

  def registerPlayer(id: String) = {
    registeredPlayers += id
  }

  def unregisterPlayer(id: String) = {
    registerPlayer -= id
  }

  def newGame(): Option[(String, String)] = {
    registerPlayer match {
      case player1 :: player2 :: remaining => {
        registeredPlayers = remaining
        Some((player1, player2))
      }
      case _ => None
    }
  }

}

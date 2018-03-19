import akka.actor._

object MatchMaking {
  case class Register(id: String)
  case object Unregister
  protected case object FindGame
}

class MatchMaking(gameRouter: ActorRef) extends Actor {
  import MatchMaking._
  
  val matchMaker = new MatchMaker()
  var players: Map[ActorRef, String] = Map.empty
  var reversePlayers: Map[String, ActorRef] = Map.empty

  def receive = {
    case Register(id) =>
      players += (sender() -> id)
      reversePlayers += (id -> sender())
      context.watch(sender())
      matchMaker.registerPlayer(id)
      if (players.size >= 2) self ! FindGame

    case Unregister =>
      unregister(sender())
      matchMaker.unregisterPlayer(players(sender()))

    case Terminated(player) =>
      unregister(player)
      matchMaker.unregisterPlayer(players(sender()))

    case FindGame =>
      matchMaker.newGame() match {
        case None => Nil
        case Some((player1, player2)) => {
          val actor1 = unregister(player1)
          val actor2 = unregister(player2)
          val newGameId = player1 + player2
          gameRouter ! GameRouter.NewGame(newGameId, actor1, actor2)
        }
      }
      if (players.size >= 2) self ! FindGame
  }
  
  private def unregister(id: String) = {
    val actor = reversePlayers(id)
    players -= actor
    reversePlayers -= id
    actor
  }

  private def unregister(actor: ActorRef) = {
    val id = players(actor)
    reversePlayers -= id
    players -= actor
    id
  }
  
}

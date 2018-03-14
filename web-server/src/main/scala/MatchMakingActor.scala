import akka.actor._

object MatchMaking {
  case class Register(id: String)
  case object Unregister
  case object FindGame
}

class MatchMakingActor extends Actor {
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
          unregister(player1)
          unregister(player2)
        }
      }
      if (players.size >= 2) self ! FindGame
  }
  
  private def unregister(id: String) = {
    val actor = reversePlayers(id)
    players -= actor
    reversePlayers -= id
  }

  private def unregister(actor: ActorRef) = {
    val id = players(actor)
    reversePlayers -= id
    players -= actor
  }
  
}

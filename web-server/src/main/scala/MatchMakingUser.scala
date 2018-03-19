import akka.actor.{Actor, ActorRef}

object MatchMakingUser {
  case class Connected(outgoing: ActorRef)
  case class IncomingMessage(text: String)
  case class OutgoingMessage(text: String)
  case class NewGame(id: String)
}

class MatchMakingUser(matchMaking: ActorRef) extends Actor {
  import MatchMakingUser._

  def receive = {
    case Connected(outgoing) =>
      context.become(connected(outgoing))
  }

  def connected(outgoing: ActorRef): Receive = {
    case IncomingMessage(text) =>
      matchMaking ! MatchMaking.Register(text)

    case NewGame(id) =>
      outgoing ! OutgoingMessage(id)
  }

}

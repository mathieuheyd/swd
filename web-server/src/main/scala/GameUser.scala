import akka.actor._

object GameUser {
  case class Connected(outgoing: ActorRef)
  case class IncomingMessage(text: String)
  case class OutgoingMessage(text: String)
}

class GameUser(gameRoom: ActorRef) extends Actor {
  import GameUser._

  def receive = {
    case Connected(outgoing) =>
      context.become(connected(outgoing))
  }

  def connected(outgoing: ActorRef): Receive = {
    gameRoom ! GameRoom.Join

    {
      case IncomingMessage(text) =>
        gameRoom ! GameRoom.ChatMessage(text)

      case GameRoom.ChatMessage(text) =>
        outgoing ! OutgoingMessage(text)
    }
  }

}

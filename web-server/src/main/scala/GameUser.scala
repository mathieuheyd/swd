import akka.actor._

object GameUser {
  case class Connected(outgoing: ActorRef)
  case class JoinGame(gameRoom: ActorRef)
  case class IncomingMessage(text: String)
  case class OutgoingMessage(text: String)
}

class GameUser() extends Actor {
  import GameUser._

  var gameRoom: Option[ActorRef] = None
  var outgoing: Option[ActorRef] = None

  def receive = {
    case Connected(out) =>
      outgoing = Some(out)
      if (gameRoom.isDefined && outgoing.isDefined)
        context.become(connected())

    case JoinGame(room) =>
      gameRoom = Some(room)
      if (gameRoom.isDefined && outgoing.isDefined)
        context.become(connected())
  }

  def connected(): Receive = {
    gameRoom.get ! GameRoom.Join

    {
      case IncomingMessage(text) =>
        gameRoom.get ! GameRoom.ChatMessage(text)

      case GameRoom.ChatMessage(text) =>
        outgoing.get ! OutgoingMessage(text)
    }
  }

}

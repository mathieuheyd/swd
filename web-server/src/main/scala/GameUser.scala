import akka.actor._
import argonaut._
import Argonaut._
import ArgonautShapeless._
import GameRoom.EventViewMessage
import entities.CardSet
import play.{ActionType, GameAction}
import view.EventView

object GameUser {
  case class Connected(outgoing: ActorRef)
  case class JoinGame(gameRoom: ActorRef)

  case class IncomingMessage(text: String)
  case class OutgoingMessage(text: String)

  sealed trait UserMessage
  case class ChatUserMessage(text: String) extends UserMessage
  case class ActionUserMessage(action: GameAction) extends UserMessage

  implicit def CardSetEncodeJson: EncodeJson[CardSet.CardSet] = EncodeJson({
    case value => value.id.asJson
  })
  implicit def ActionTypeEncodeJson: EncodeJson[ActionType.ActionType] = EncodeJson({
    case value => value.toString.asJson
  })

  val eventViewEncoder = EncodeJson.of[EventView]
  implicit def userMessageDecoder = DecodeJson.of[UserMessage]
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
        val message = Parse.decode[UserMessage](text)
        if (message.isRight) {
          message.right.get match {
            case ChatUserMessage(chatMessage) => gameRoom.get ! GameRoom.ChatMessage(chatMessage)
            case ActionUserMessage(action) => gameRoom.get ! GameRoom.PlayerActionMessage(action)
          }
        } else {
          Console.out.println("Badly fornatted message", text)
        }

      case GameRoom.ChatMessage(text) =>
        outgoing.get ! OutgoingMessage(text)

      case message: EventViewMessage =>
        outgoing.get ! OutgoingMessage(eventViewEncoder(message.event).nospaces)
    }
  }

}

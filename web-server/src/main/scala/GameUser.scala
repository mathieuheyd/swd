import akka.actor._
import argonaut._
import Argonaut._
import ArgonautShapeless._
import GameRoom.{EventViewMessage, GameStart}
import entities.CardSet
import play.GameAction
import view.EventView

object GameUser {
  case class Connected(outgoing: ActorRef)
  case class JoinGame(gameRoom: ActorRef)

  case class IncomingMessage(text: String)
  case class OutgoingMessage(text: String)

  sealed trait UserMessage
  case class ChatUserMessage(text: String) extends UserMessage
  case class ActionUserMessage(action: GameAction) extends UserMessage

  implicit def GameStartEncodeJson: EncodeJson[GameStart] =
    EncodeJson(gameStart =>
      ("event" := "game_start") ->:
      ("player_name" := gameStart.playerName) ->:
      ("opponent_name" := gameStart.playerName) ->: jEmptyObject)

  implicit def CardSetEncodeJson: EncodeJson[CardSet.CardSet] = EncodeJson({
    case value => value.toString.asJson
  })

  val eventViewEncoder = EncodeJson.of[EventView]
  val userMessageDecoder = DecodeJson.of[UserMessage]
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

      case event: GameStart =>
        outgoing.get ! OutgoingMessage(event.asJson.nospaces)

      case message: EventViewMessage =>
        outgoing.get ! OutgoingMessage(eventViewEncoder(message.event).nospaces)
    }
  }

}

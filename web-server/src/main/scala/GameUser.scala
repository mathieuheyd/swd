import akka.actor._
import argonaut._
import Argonaut._
import ArgonautShapeless._
import GameRoom.EventViewMessage
import entities.{CardId, CardSet}
import view.EventView

object GameUser {
  case class Connected(outgoing: ActorRef)
  case class JoinGame(gameRoom: ActorRef)

  case class IncomingMessage(text: String)
  case class OutgoingMessage(text: String)

  case class GameStart(playerName: String, opponentName: String)

  implicit def GameStartEncodeJson: EncodeJson[GameStart] =
    EncodeJson(gameStart =>
      ("event" := "game_start") ->:
      ("player_name" := gameStart.playerName) ->:
      ("opponent_name" := gameStart.playerName) ->: jEmptyObject)

  implicit def CardSetEncodeJson: EncodeJson[CardSet.CardSet] = EncodeJson({
    case _ => "CardSet".asJson
  })

  val eventViewEncoder = EncodeJson.of[EventView]
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

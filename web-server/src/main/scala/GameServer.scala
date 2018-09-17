import java.net.{HttpURLConnection, URL}

import akka.NotUsed
import akka.actor._
import akka.http.scaladsl._
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, MediaTypes, StatusCodes}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.io.StdIn

object GameServer {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    val gameRouter = system.actorOf(Props(new GameRouter), "game-router")
    val matchMaking = system.actorOf(Props(new MatchMaking(gameRouter)), "match-making")

    def newGameUser(gameId: String): Flow[Message, Message, NotUsed] = {
      val userActor = system.actorOf(Props(new GameUser))
      gameRouter ! GameRouter.UserJoin(gameId, userActor)

      val incomingMessages: Sink[Message, NotUsed] =
        Flow[Message].map {
          case TextMessage.Strict(text) => GameUser.IncomingMessage(text)
        }.to(Sink.actorRef[GameUser.IncomingMessage](userActor, PoisonPill))

      val outgoingMessages: Source[Message, NotUsed] =
        Source.actorRef[GameUser.OutgoingMessage](10, OverflowStrategy.fail)
          .mapMaterializedValue { outActor =>
            userActor ! GameUser.Connected(outActor)
            NotUsed
          }.map(
          (outMsg: GameUser.OutgoingMessage) => TextMessage(outMsg.text))

      Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
    }

    def newMatchMaking(): Flow[Message, Message, NotUsed] = {
      val userActor = system.actorOf(Props(new MatchMakingUser(matchMaking)))

      val incomingMessages: Sink[Message, NotUsed] =
        Flow[Message].map {
          case TextMessage.Strict(text) => MatchMakingUser.IncomingMessage(text)
        }.to(Sink.actorRef[MatchMakingUser.IncomingMessage](userActor, PoisonPill))

      val outgoingMessages: Source[Message, NotUsed] =
        Source.actorRef[MatchMakingUser.OutgoingMessage](10, OverflowStrategy.fail)
          .mapMaterializedValue { outActor =>
            userActor ! MatchMakingUser.Connected(outActor)
            NotUsed
          }.map(
          (outMsg: MatchMakingUser.OutgoingMessage) => TextMessage(outMsg.text))

      Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
    }

    val route =
      path("match") {
        get {
          handleWebSocketMessages(newMatchMaking())
        }
      } ~
      pathPrefix("game") {
        path(Remaining) { gameId =>
          get {
            handleWebSocketMessages(newGameUser(gameId))
          }
        }

      } ~
      path("cards") {
        get {
          complete(HttpResponse(
            StatusCodes.OK,
            entity = HttpEntity(MediaTypes.`application/json`, CardsController.getAllCardsDescription().nospaces)
          ))
        }
      } ~
      pathPrefix("client") {
        getFromResourceDirectory("client")
      } ~
      pathPrefix("card") {
        path(Remaining) { remaining =>
          get {
            val url = new URL("http://swdestinydb.com/bundles/cards/" + remaining)
            val connection = url.openConnection().asInstanceOf[HttpURLConnection]
            connection.setRequestMethod("GET")
            val in = connection.getInputStream
            val byteArray = Stream.continually(in.read).takeWhile(-1 !=).map(_.toByte).toArray
            complete(HttpResponse(
              StatusCodes.OK,
              entity = HttpEntity(MediaTypes.`image/jpeg`, byteArray)
            ))
          }
        }
      }

    val binding = Await.result(Http().bindAndHandle(route, "127.0.0.1", 8080), 3.seconds)


    // the rest of the sample code will go here
    println("Started server at 127.0.0.1:8080, press enter to kill server")
    StdIn.readLine()
    system.terminate()
  }
}
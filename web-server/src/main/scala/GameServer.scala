import akka.NotUsed
import akka.actor._
import akka.http.scaladsl._
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

    val matchMaking = system.actorOf(Props(new MatchMaking), "match-making")
    val ganeRouter = system.actorOf(Props(new GameRouter), "game-router")

    val gameRoom = system.actorOf(Props(new GameRoom), "game")

    def newUser(): Flow[Message, Message, NotUsed] = {
      // new connection - new user actor
      val userActor = system.actorOf(Props(new GameUser(gameRoom)))

      val incomingMessages: Sink[Message, NotUsed] =
        Flow[Message].map {
          // transform websocket message to domain message
          case TextMessage.Strict(text) => GameUser.IncomingMessage(text)
        }.to(Sink.actorRef[GameUser.IncomingMessage](userActor, PoisonPill))

      val outgoingMessages: Source[Message, NotUsed] =
        Source.actorRef[GameUser.OutgoingMessage](10, OverflowStrategy.fail)
        .mapMaterializedValue { outActor =>
          // give the user actor a way to send messages out
          userActor ! GameUser.Connected(outActor)
          NotUsed
        }.map(
          // transform domain message to web socket message
          (outMsg: GameUser.OutgoingMessage) => TextMessage(outMsg.text))

      // then combine both to a flow
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
      path("game") {
        get {
          handleWebSocketMessages(newUser())
        }
      } ~
      pathPrefix("client") {
        getFromResourceDirectory("client")
      }

    val binding = Await.result(Http().bindAndHandle(route, "127.0.0.1", 8080), 3.seconds)


    // the rest of the sample code will go here
    println("Started server at 127.0.0.1:8080, press enter to kill server")
    StdIn.readLine()
    system.terminate()
  }
}
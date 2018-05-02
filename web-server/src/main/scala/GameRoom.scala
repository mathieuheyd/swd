import akka.actor._
import collection.{StarterKyloRen, StarterRey}
import play.{GameAction, GameMechanics}
import view.{EventView, GameController}

object GameRoom {
  case object Join
  case class ChatMessage(message: String)

  case class GameStart(playerName: String, opponentName: String)
  case class EventViewMessage(event: EventView)

  case class PlayerActionMessage(action: GameAction)
}

class GameRoom(player1: String, player2: String) extends Actor {
  import GameRoom._

  var actorPlayer1: Option[ActorRef] = None
  var actorPlayer2: Option[ActorRef] = None
  val game = new GameMechanics(StarterRey.deck, StarterKyloRen.deck)
  val controller = new GameController(game)

  def receive = {
    case Join =>
      if (actorPlayer1.isEmpty) actorPlayer1 = Some(sender())
      else if (actorPlayer2.isEmpty) actorPlayer2 = Some(sender())
      context.watch(sender())

      if (actorPlayer1.isDefined && actorPlayer2.isDefined) startGame()

    case Terminated(user) =>
      if (actorPlayer1.contains(user)) actorPlayer1 = None
      if (actorPlayer2.contains(user)) actorPlayer2 = None

    case msg: ChatMessage =>
      Seq(actorPlayer1, actorPlayer2).flatten.foreach(_ ! msg)
  }

  def startGame() = {
    Console.out.println(actorPlayer1)
    Console.out.println(actorPlayer2)
    actorPlayer1.get ! GameStart(player1, player2)
    actorPlayer2.get ! GameStart(player2, player1)

    val setupEvents = controller.startGame()
    setupEvents.foreach { bothSideEvent =>
      actorPlayer1.get ! EventViewMessage(bothSideEvent.player1)
      actorPlayer2.get ! EventViewMessage(bothSideEvent.player2)
    }
  }
}
import akka.actor._
import collection.{StarterKyloRen, StarterRey}
import play.{GameAction, Player}
import view.{BothSideEventView, EventView, GameController, PlayerInfo}

object GameRoom {
  case object Join
  case class ChatMessage(message: String)

  case class EventViewMessage(event: EventView)

  case class PlayerActionMessage(action: GameAction)
}

class GameRoom(player1: String, player2: String) extends Actor {
  import GameRoom._

  var actorPlayer1: Option[ActorRef] = None
  var actorPlayer2: Option[ActorRef] = None
  val controller = new GameController(
    PlayerInfo("Mathieu", StarterRey.deck),
    PlayerInfo("Laumer", StarterKyloRen.deck)
  )

  def receive = {
    case Join =>
      if (actorPlayer1.isEmpty) actorPlayer1 = Some(sender())
      else if (actorPlayer2.isEmpty) actorPlayer2 = Some(sender())
      context.watch(sender())

      if (actorPlayer1.isDefined && actorPlayer2.isDefined) startGame()

    case Terminated(user) =>
      if (actorPlayer1.contains(user)) actorPlayer1 = None
      if (actorPlayer2.contains(user)) actorPlayer2 = None

    case chatMessage: ChatMessage =>
      Seq(actorPlayer1, actorPlayer2).flatten.foreach(_ ! chatMessage)

    case actionMessage: PlayerActionMessage =>
      val events = controller.playerAction(sendingPlayer(sender()), actionMessage.action)
      sendBothEvents(events)
  }

  private def startGame() = {
    val setupEvents = controller.startGame()
    sendBothEvents(setupEvents)
  }

  private def sendBothEvents(eventsView: Seq[BothSideEventView]) = {
    eventsView.foreach { bothSideEvent =>
      actorPlayer1.get ! EventViewMessage(bothSideEvent.player1)
      actorPlayer2.get ! EventViewMessage(bothSideEvent.player2)
    }
  }

  private def sendingPlayer(sender: ActorRef): Player.Value = {
    if (actorPlayer1.contains(sender)) return Player.Player1
    if (actorPlayer2.contains(sender)) return Player.Player2
    throw new Exception("Invalid sender")
  }
}
import akka.actor._
import collection.{StarterKyloRen, StarterRey}
import play.GameMechanics

object GameRoom {
  case object Join
  case class ChatMessage(message: String)
}

class GameRoom(player1: String, player2: String) extends Actor {
  import GameRoom._

  var users: Set[ActorRef] = Set.empty
  val game = new GameMechanics(StarterRey.deck, StarterKyloRen.deck)

  def receive = {
    case Join =>
      users += sender()
      context.watch(sender())

    case Terminated(user) =>
      users -= user

    case msg: ChatMessage =>
      users.foreach(_ ! msg)
  }
}
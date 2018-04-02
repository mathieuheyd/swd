import akka.actor.{Actor, ActorRef, Props}

case class GameActors(gameRoom: ActorRef, player1: ActorRef, player2: ActorRef)

object GameRouter {
  case class NewGame(id: String, player1: ActorRef, player2: ActorRef)
  case class UserJoin(gameId: String, user: ActorRef)
}

class GameRouter extends Actor {
  import GameRouter._

  var games: Map[String, GameActors] = Map.empty

  def receive = {
    case NewGame(id, player1, player2) => {
      val newGame = context.actorOf(Props(new GameRoom("player1", "player2")), "game" + id)
      games += (id -> GameActors(newGame, player1, player2))
      player1 ! MatchMakingUser.NewGame(id)
      player2 ! MatchMakingUser.NewGame(id)
    }

    case UserJoin(gameId, user) => {
      val game = games(gameId)
      user ! GameUser.JoinGame(game.gameRoom)
    }
  }

}

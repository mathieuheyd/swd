import org.scalatest.FlatSpec

class MatchMakerSpec extends FlatSpec {

  "Match making" should "create a game when there is enough people" in {
    val matchMaker = new MatchMaker

    matchMaker.registerPlayer("player1")
    assert(matchMaker.newGame() == None)

    matchMaker.registerPlayer("player2")
    assert(matchMaker.newGame() == Some(("player1", "player2")))

    matchMaker.registerPlayer("player3")
    assert(matchMaker.newGame() == None)

    matchMaker.registerPlayer("player4")
    matchMaker.registerPlayer("player5")
    assert(matchMaker.newGame() == Some(("player3", "player4")))
  }

  "Match making" should "let people unregister" in {
    val matchMaker = new MatchMaker

    matchMaker.registerPlayer("player1")
    assert(matchMaker.newGame() == None)

    matchMaker.registerPlayer("player2")
    matchMaker.unregisterPlayer("player1")
    assert(matchMaker.newGame() == None)
  }

}

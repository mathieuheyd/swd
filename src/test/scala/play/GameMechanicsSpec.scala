package play

import collection.{StarterKyloRen, StarterRey}
import org.scalatest.FlatSpec

class GameMechanicsSpec extends FlatSpec {

  "Both player area" should "be initialized" in {
    val mechanics = new GameMechanics(StarterRey.deck, StarterKyloRen.deck)
    assert(mechanics.phase == GamePhase.Setup)
  }

//  "Starting hands" should "put 5 cards in hand" in {
//    val mechanics = new GameMechanics(StarterRey.deck, StarterKyloRen.deck)
//    mechanics.drawStartingHands()
//    assert(mechanics.areaPlayer1.hand.size == 5)
//    assert(mechanics.areaPlayer2.hand.size == 5)
//    assert(mechanics.eventsHistory.init.last == DrawEvent(Player.Player1, mechanics.areaPlayer1.hand.map(_.uniqueId)))
//    assert(mechanics.eventsHistory.last == DrawEvent(Player.Player2, mechanics.areaPlayer2.hand.map(_.uniqueId)))
//    assert(mechanics.phase == GamePhase.Mulligan)
//  }
//
//  "Mulligan phase" should "be handled properly" in {
//    val mechanics = new GameMechanics(StarterRey.deck, StarterKyloRen.deck)
//    mechanics.drawStartingHands()
//
//    mechanics.handleAction(Player.Player1, MulliganAction(Seq.empty))
//    assert(mechanics.eventsHistory.last == MulliganEvent(Player.Player1, Seq.empty))
//
//    val hand = mechanics.areaPlayer2.hand
//    val mulliganCards = Seq(hand(1).uniqueId, hand(3).uniqueId)
//    mechanics.handleAction(Player.Player2, MulliganAction(mulliganCards))
//    assert(mechanics.eventsHistory.init.last == MulliganEvent(Player.Player2, mulliganCards))
//    assert(mechanics.areaPlayer2.hand.size == 5)
//    val newCards = mechanics.areaPlayer2.hand.diff(Seq(hand(0), hand(2), hand(4)))
//    assert(mechanics.eventsHistory.last == DrawEvent(Player.Player2, newCards.map(_.uniqueId)))
//    assert(mechanics.phase == GamePhase.Battlefield)
//  }

}

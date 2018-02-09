package play

trait GameEvent

case class MulliganEvent(val player: Player.Value, val cards: Seq[Int]) extends GameEvent

case class DrawEvent(val player: Player.Value, val cards: Seq[Int]) extends GameEvent

case class PassEvent(val player: Player.Value) extends GameEvent


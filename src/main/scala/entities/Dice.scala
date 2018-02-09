package entities

object DiceSideSymbol extends Enumeration {
  type DiceSideSynbol = Value
  val MeleeDamage, RangedDamage, Shield, Resource, Disrupt, Discard, Focus, Special, Blank = Value
}

case class DiceSide(symbol: DiceSideSymbol.Value, value: Int = 0, modifier: Boolean = false, cost: Int = 0)

case class Dice(side1: DiceSide,
                side2: DiceSide,
                side3: DiceSide,
                side4: DiceSide,
                side5: DiceSide,
                side6: DiceSide)

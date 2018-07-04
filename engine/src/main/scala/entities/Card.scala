package entities

object CardSet extends Enumeration {
  type CardSet = Value
  val Awakenings = Value(1)
  val SpiritOfRebellion = Value(2)
  val EmpireAtWar = Value(3)
  val TwoPlayersGame = Value(4)
}

case class CardId(set: CardSet.Value, id: Int)

object Affiliation extends Enumeration {
  type Affiliation = Value
  val Hero, Villain, Neutral = Value
}

object CardColor extends Enumeration {
  type CardColor = Value
  val Red, Blue, Yellow, Gray = Value
}

object CardType extends Enumeration {
  type CardType = Value
  val Battlefield, Character, Event, Upgrade, Support = Value
}

object CardRarity extends Enumeration {
  type CardRarity = Value
  val Fixed, Common, Uncommon, Rare, Legendary = Value
}

trait Card {
  val id: CardId
  val title: String
  val rarity: CardRarity.Value
  val color: CardColor.Value
  val cardType: CardType.Value
}

trait WithSubtitle {
  val subtitle: Option[String]
}

trait WithAffiliation {
  val affiliation: Affiliation.Value
}

trait WithUniqueness {
  val unique: Boolean
}

trait WithDice {
  val dice: Option[Dice]
}

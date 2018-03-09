package entities

case class CharacterPoints(primary: Int, elite: Option[Int])

case class Character(val id: CardId,
                     val title: String,
                     val subtitle: Option[String],
                     val affiliation: Affiliation.Value,
                     val color: CardColor.Value,
                     val unique: Boolean,
                     val rarity: CardRarity.Value,
                     val dice: Option[Dice],
                     val points: CharacterPoints,
                     val maxHealth:Int)
  extends Card with WithSubtitle with WithUniqueness with WithAffiliation with WithDice {

  val cardType: CardType.Value = CardType.Character

  def canElite(): Boolean = {
    return !points.elite.isEmpty
  }

  def points(elite: Boolean): Int = {
    if (!elite) points.primary else points.elite.get
  }

}

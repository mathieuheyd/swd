package entities

case class CharacterPoints(primary: Int, elite: Option[Int])

case class Character(id: CardId,
                     title: String,
                     subtitle: Option[String],
                     affiliation: Affiliation.Value,
                     color: CardColor.Value,
                     unique: Boolean,
                     rarity: CardRarity.Value,
                     dice: Option[Dice],
                     points: CharacterPoints,
                     maxHealth:Int)
  extends Card with WithSubtitle with WithUniqueness with WithAffiliation with WithDice {

  val cardType: CardType.Value = CardType.Character

  def canElite: Boolean = {
    points.elite.isDefined
  }

  def points(elite: Boolean): Int = {
    if (!elite) points.primary else points.elite.get
  }

}

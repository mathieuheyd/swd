package entities

case class Battlefield(val id:CardId,
                       val title: String,
                       val subtitle: Option[String],
                       val rarity: CardRarity.Value)
  extends Card with WithSubtitle {

  val color: CardColor.Value = CardColor.Gray

  val cardType: CardType.Value = CardType.Battlefield

}

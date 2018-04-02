package entities

case class Battlefield(id:CardId,
                       title: String,
                       subtitle: Option[String],
                       rarity: CardRarity.Value)
  extends Card with WithSubtitle {

  val color: CardColor.Value = CardColor.Gray

  val cardType: CardType.Value = CardType.Battlefield

}

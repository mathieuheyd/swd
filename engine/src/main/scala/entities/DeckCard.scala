package entities

case class DeckCard(val id: CardId,
                    val title: String,
                    val affiliation: Affiliation.Value,
                    val color: CardColor.Value,
                    val unique: Boolean,
                    val rarity: CardRarity.Value,
                    val cardType: CardType.Value,
                    val dice: Option[Dice],
                    val cost: Int)
  extends Card with WithAffiliation with WithDice {

}

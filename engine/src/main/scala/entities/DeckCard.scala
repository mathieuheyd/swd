package entities

case class DeckCard(id: CardId,
                    title: String,
                    affiliation: Affiliation.Value,
                    color: CardColor.Value,
                    unique: Boolean,
                    rarity: CardRarity.Value,
                    cardType: CardType.Value,
                    dice: Option[Dice],
                    cost: Int)
  extends Card with WithAffiliation with WithDice {

}

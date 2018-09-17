import argonaut._
import Argonaut._
import ArgonautShapeless._
import collection.Cards
import entities.{Affiliation, Battlefield, CardColor, CardId, CardRarity, CardSet, CardType, Character, DeckCard, Dice, DiceSideSymbol}

object CardsController {

  implicit def CardSetEncodeJson: EncodeJson[CardSet.CardSet] = EncodeJson({
    case value => value.id.asJson
  })
  implicit def CardColorEncodeJson: EncodeJson[CardColor.CardColor] = EncodeJson({
    case value => value.id.asJson
  })
  implicit def AffiliationEncodeJson: EncodeJson[Affiliation.Affiliation] = EncodeJson({
    case value => value.id.asJson
  })
  implicit def CardRarityEncodeJson: EncodeJson[CardRarity.CardRarity] = EncodeJson({
    case value => value.id.asJson
  })
  implicit def CardTypeEncodeJson: EncodeJson[CardType.CardType] = EncodeJson({
    case value => value.id.asJson
  })
  implicit def DiceSideSymbolEncodeJson: EncodeJson[DiceSideSymbol.DiceSideSymbol] = EncodeJson({
    case value => value.id.asJson
  })

  implicit val cardDescriptionEncoder = EncodeJson.of[CardDescription]

  case class CardDescription(id: CardId,
                             color: CardColor.Value,
                             dice: Option[Dice])

  def getAllCardsDescription() = {
    Cards.allCards.map {
      case card: Character => CardDescription(card.id, card.color, card.dice)
      case card: DeckCard => CardDescription(card.id, card.color, card.dice)
      case card: Battlefield => CardDescription(card.id, card.color, None)
    }.toList.asJson
  }

}

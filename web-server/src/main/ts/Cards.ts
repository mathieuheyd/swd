/// <reference path="reference.ts" />

enum CardColor {
  Red,
  Blue,
  Yellow,
  Gray
}

enum DiceSymbol {
  MeleeDamage,
  RangedDamage,
  Shield,
  Resource,
  Disrupt,
  Discard,
  Focus,
  Special,
  Blank
}

class CardDescription {
  id: CardId;
  color: CardColor;
  dice: DiceDescription;
}

class DiceDescription {
  side1: DiceSide;
  side2: DiceSide;
  side3: DiceSide;
  side4: DiceSide;
  side5: DiceSide;
  side6: DiceSide;
}

class DiceSide {
  symbol: DiceSymbol;
  value: number;
  modifier: boolean;
  cost: number;
}

class Cards {

  descriptions: Map<CardId, CardDescription>;

  loadCards() {
    fetch("/cards")
      .then(function(response) { return response.json(); })
      .then(function(cards) {
        this.descriptions = new Map<CardId, CardDescription>();
        console.log(cards, typeof(cards));
        for (let card of cards) {
          this.descriptions.set(card.id, card);
        }
        console.log(this.descriptions);
      });
  }

}
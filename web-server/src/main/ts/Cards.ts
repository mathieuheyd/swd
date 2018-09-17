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

  descriptions: Map<number, CardDescription> = new Map();

  key(id: CardId) {
    return id.set * 1000 + id.id;
  }

  get(id: CardId) {
    this.descriptions.get(this.key(id));
  }

  loadCards(onLoaded: Function) {
    fetch("/cards")
      .then(function(response) { return response.json(); })
      .then((cards) => this.setAll(cards))
      .then(() => onLoaded());
  }

  setAll(cards: any) {
    this.descriptions = new Map<number, CardDescription>();
    for (let card of cards) {
      this.descriptions.set(this.key(card.id), card);
    }
  }

}
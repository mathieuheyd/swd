/// <reference path="../reference.ts" />

class HandInterface extends PIXI.Container {

  cards: Array<CardView>;

  constructor() {
    super();
    this.cards = [];
  }

  addCard(card: CardView) {
    this.cards.push(card);

    this.removeChildren();
    for (let c of this.cards) {
      this.addChild(new CardInterface(c.card));
    }
  }

}

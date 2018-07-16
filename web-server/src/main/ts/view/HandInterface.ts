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

    let offset: number = 0;
    for (let c of this.cards) {
      let cardInterface = new CardInterface(c.card);
      cardInterface.x = offset;
      this.addChild(cardInterface);
      offset += 70;
    }
  }

}

/// <reference path="../reference.ts" />

class HandOpponentInterface extends PIXI.Container {

  cards: number;
  cardsInterfaces: Array<CardHiddenInterface>;

  constructor() {
    super();
    this.cards = 0;
  }

  addCard() {
    this.cards += 1;
    this.updateDisplay();
  }

  removeCard() {
    this.cards -= 1;
    this.updateDisplay();
  }

  updateDisplay() {
    this.removeChildren();
    this.cardsInterfaces = Array();

    let offset: number = 0;
    for (let i = 0; i < this.cards; ++i) {
      let cardInterface = new CardHiddenInterface();
      cardInterface.x = offset;
      this.addChild(cardInterface);
      offset += 70;
      this.cardsInterfaces.push(cardInterface);
    }
  }

}

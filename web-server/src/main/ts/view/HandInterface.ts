/// <reference path="../reference.ts" />

class HandInterface extends PIXI.Container {

  cards: Array<CardView>;
  cardsInterfaces: Array<CardInterface>;

  cardsToMulligan: Set<Number>;

  constructor() {
    super();
    this.cards = [];
  }

  addCard(card: CardView) {
    this.cards.push(card);

    this.removeChildren();
    this.cardsInterfaces = Array();

    let offset: number = 0;
    for (let c of this.cards) {
      let cardInterface = new CardInterface(c);
      cardInterface.x = offset;
      this.addChild(cardInterface);
      offset += 70;
      this.cardsInterfaces.push(cardInterface);
    }
  }

  startMulligan() {
    this.cardsToMulligan = new Set();

    for (let card of this.cardsInterfaces) {
      card.interactive = true;
      card.on('click', () => this.selectCardToMulligan(card));
    }
  }

  selectCardToMulligan(card: CardInterface) {
    let uniqueId = card.card.uniqueId;
    console.log('card clicked', uniqueId);
    if (this.cardsToMulligan.has(uniqueId)) {
      this.cardsToMulligan.delete(uniqueId);
    } else {
      this.cardsToMulligan.add(uniqueId);
    }
  }

}

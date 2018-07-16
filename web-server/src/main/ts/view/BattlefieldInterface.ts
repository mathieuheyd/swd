/// <reference path="../reference.ts" />

class BattlefieldInterface extends PIXI.Container {

  constructor(card: CardView) {
    super();
    this.addChild(new CardInterface(card.card));
  }

}

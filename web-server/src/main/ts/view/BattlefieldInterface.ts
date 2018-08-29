/// <reference path="../reference.ts" />

class BattlefieldInterface extends PIXI.Container {

  card: CardView;
  cardInterface: CardInterface;

  constructor(card: CardView) {
    super();
    this.setCard(card);
  }

  setCard(card: CardView) {
    this.card = card;
    this.updateDisplay();
  }

  updateDisplay() {
    this.removeChildren();
    this.cardInterface = null;
    if (this.card  != null) {
      this.cardInterface = new CardInterface(this.card);
      this.addChild(this.cardInterface);
    }
  }

  startChoose(onChoose: Function) {
    this.cardInterface.interactive = true;
    this.cardInterface.on('click', () => onChoose(this.card.uniqueId));
  }

  stopChoose() {
    this.updateDisplay();
  }

}

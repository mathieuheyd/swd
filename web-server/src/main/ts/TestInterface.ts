/// <reference path="reference.ts" />

class TestApplication {
  view: PIXI.Application;

  constructor() {
    this.view = new PIXI.Application(800, 600, {backgroundColor : 0x1099bb});
    document.body.appendChild(this.view.view);

    let d = new DiceView();
    let cardId = new CardId();
    cardId.set = 1;
    cardId.id = 1;
    d.card = cardId;
    let dice = new DiceInterface(d);
    dice.x = 100;
    dice.y = 100;
    dice.width = 100;
    dice.height = 100;
    this.view.stage.addChild(dice);
  }
}

function testInterface() {
  new TestApplication();
}
/// <reference path="reference.ts" />

class TestApplication {
  view: PIXI.Application;

  constructor() {
    this.view = new PIXI.Application(1200, 700, {backgroundColor : 0x1099bb});
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

    let c = new CharacterView();
    c.card = cardId;
    let character = new CharacterInterface(c);
    character.x = 100;
    character.y = 300;
    character.width = 360;
    character.height = 250;
    this.view.stage.addChild(character);

    let upgrade = new CardView();
    upgrade.card = { set: 1, id: 7};
    character.addUpgrade(upgrade);

    let upgrade2 = new CardView();
    upgrade2.card = { set: 1, id: 8};
    character.addUpgrade(upgrade2);

    character.activateCharacter();
  }
}

function testInterface() {
  new TestApplication();
}
/// <reference path="reference.ts" />

class TestApplication {
  view: PIXI.Application;

  constructor() {
    this.view = new PIXI.Application(1200, 700, {backgroundColor : 0x1099bb});
    document.body.appendChild(this.view.view);

    let d = new DiceView();
    d.card = { set: 1, id: 1};
    let dice = new DiceInterface(d);
    dice.x = 100;
    dice.y = 100;
    dice.width = 100;
    dice.height = 100;
    this.view.stage.addChild(dice);

    let c = new CharacterView();
    c.card = { set: 1, id: 1};
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

    let diceCharacter = new DiceView();
    diceCharacter.card = { set: 1, id: 1};
    character.addDice(diceCharacter);

    let diceUpgrade = new DiceView();
    diceUpgrade.card = { set: 1, id: 7};
    character.addDice(diceUpgrade);

    let diceUpgrade2 = new DiceView();
    diceUpgrade2.card = { set: 1, id: 8};
    character.addDice(diceUpgrade2);

    //character.activateCharacter();
  }
}

function testInterface() {
  new TestApplication();
}
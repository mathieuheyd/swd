/// <reference path="reference.ts" />

class TestApplication {
  view: PIXI.Application;

  constructor(cards: Cards) {
    this.view = new PIXI.Application(1200, 700, {backgroundColor : 0x1099bb});
    document.body.appendChild(this.view.view);

    let c = new CharacterView();
    c.card = { set: 1, id: 1};
    let character = new CharacterInterface(c);
    character.x = 100;
    character.y = 300;
    character.width = 360;
    character.height = 325;
    this.view.stage.addChild(character);

    let upgrade = new CardView();
    upgrade.card = { set: 1, id: 17};
    character.addUpgrade(upgrade);

    let upgrade2 = new CardView();
    upgrade2.card = { set: 1, id: 8};
    character.addUpgrade(upgrade2);

    let diceCharacter = new DiceFullView();
    diceCharacter.uniqueId = 101;
    diceCharacter.card = { set: 1, id: 1};
    diceCharacter.description = cards.get(diceCharacter.card);
    character.addDice(diceCharacter);

    let diceUpgrade = new DiceFullView();
    diceUpgrade.uniqueId = 117;
    diceUpgrade.card = { set: 1, id: 17};
    diceUpgrade.description = cards.get(diceUpgrade.card);
    character.addDice(diceUpgrade);

    let diceUpgrade2 = new DiceFullView();
    diceUpgrade2.uniqueId = 108;
    diceUpgrade2.card = { set: 1, id: 8};
    diceUpgrade2.description = cards.get(diceUpgrade2.card);
    character.addDice(diceUpgrade2);

    character.activateCharacter();
    character.damages = 11;
    character.putDiceInPool(101);
    character.putDiceInPool(117);
    for (let dice of character.dices) {
      dice.setSide(3);
    }
  }
}

function testInterface() {
  let cards = new Cards();
  cards.loadCards(function() { new TestApplication(cards); });
}
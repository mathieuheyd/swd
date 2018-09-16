/// <reference path="../reference.ts" />

class CharacterInterface extends PIXI.Container {

  character: CardInterface;
  activated: boolean = false;

  upgrades: Array<CardInterface> = [];

  dices: Array<DiceInterface> = [];

  damages: number = 0;

  shields: number = 0;

  constructor(character: CharacterView) {
    super();

    this.character = new CardInterface(character);

    this.updateDisplay();
  }

  addDice(dice: DiceView) {
    let diceInterface = new DiceInterface(dice);
    this.dices.push(diceInterface);
    this.updateDisplay();
  }

  putDiceInPool(uniqueId: number) {
    let dice = this.dices.find(function(d) { return d.dice.uniqueId == uniqueId});
    dice.inPool = true;
    this.updateDisplay();
  }

  addUpgrade(card: CardView) {
    let cardInterface = new CardInterface(card);
    this.upgrades.push(cardInterface);
    this.updateDisplay();
  }

  updateDamages(amount: number) {
    this.damages += amount;
    this.updateDisplay();
  }

  updateShields(amount: number) {
    this.shields += amount;
    this.updateDisplay();
  }

  activateCharacter() {
    this.activated = true;
    this.character.exhausted = true;
    this.character.updateDisplay();
  }

  readyCharacter() {
    this.activated = false;
    this.character.exhausted = false;
    this.character.updateDisplay();
  }

  updateDisplay() {
    this.removeChildren();

    let background = new PIXI.Sprite(PIXI.Texture.EMPTY);
    background.width = 720;
    background.height = 650;
    this.addChild(background);

    for (let i = this.upgrades.length - 1; i >= 0; i--) {
      let upgrade = this.upgrades[i];
      upgrade.x = 300 - (i + 1) * 100;
      upgrade.y = 230;
      upgrade.width = 300;
      upgrade.height = 420;
      this.addChild(upgrade);
    }

    this.character.x = 300;
    this.character.y = 150;
    if (this.activated) {
      this.character.width = 420;
      this.character.height = 300;
    } else {
      this.character.width = 300;
      this.character.height = 420;
    }
    this.addChild(this.character);

    for (let i = 0; i < this.dices.length; i++) {
      let dice = this.dices[i];
      dice.width = 100;
      dice.height = 100;
      if (dice.inPool) {
        dice.x = 720 - ((i + 1) * 125);
        dice.y = 25;
      } else {
        dice.x = 360 + 125 * (i % 2);
        dice.y = 250 + 125 * Math.floor(i / 2);
      }
      this.addChild(dice);
    }
  }

}

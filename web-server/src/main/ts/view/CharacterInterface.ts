/// <reference path="../reference.ts" />

class CharacterInterface extends PIXI.Container {

  character: CardInterface;
  isOpponent: boolean;
  activated: boolean = false;

  upgrades: Array<CardInterface> = [];

  dices: Array<DiceInterface> = [];

  damages: number = 0;

  shields: number = 0;

  constructor(character: CharacterView, isOpponent: boolean = false) {
    super();

    this.character = new CardInterface(character);
    this.isOpponent = isOpponent;

    this.updateDisplay();
  }

  addDice(dice: DiceFullView) {
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
    this.updateDisplay();
  }

  readyCharacter() {
    this.activated = false;
    this.character.exhausted = false;
    this.character.updateDisplay();
    this.updateDisplay();
  }

  updateDisplay() {
    this.removeChildren();

    let widthOffset = 720 - this.realWidth();

    let background = new PIXI.Sprite(PIXI.Texture.EMPTY);
    background.width = 720 - widthOffset;
    background.height = 650;
    this.addChild(background);

    for (let i = this.upgrades.length - 1; i >= 0; i--) {
      let upgrade = this.upgrades[i];
      upgrade.x = 300 - (i + 1) * 100 - widthOffset;
      upgrade.y = this.isOpponent ? 0 : 230;
      upgrade.width = 300;
      upgrade.height = 420;
      this.addChild(upgrade);
    }

    this.character.x = 300 - widthOffset;
    if (this.activated) {
      this.character.y = this.isOpponent ? 200 : 150;
      this.character.width = 420;
      this.character.height = 300;
    } else {
      this.character.y = this.isOpponent ? 80 : 150;
      this.character.width = 300;
      this.character.height = 420;
    }
    this.addChild(this.character);

    let damage1 = this.damages % 3;
    let damage3 = (this.damages - damage1) / 3;
    for (let i = 0; i < damage3; i++) {
      let image: PIXI.Sprite = PIXI.Sprite.fromImage("damage_3.png");
      image.x = 420 + i * 50 - widthOffset;
      image.y = this.isOpponent ? 0 : 550;
      image.width = 100;
      image.height = 100;
      this.addChild(image);
    }
    for (let i = 0; i < damage1; i++) {
      let image: PIXI.Sprite = PIXI.Sprite.fromImage("damage_1.png");
      image.x = 420 + damage3 * 50 + i * 50 - widthOffset;
      image.y = this.isOpponent ? 0 : 550;
      image.width = 100;
      image.height = 100;
      this.addChild(image);
    }

    for (let i = 0; i < this.shields; i++) {
      let image: PIXI.Sprite = PIXI.Sprite.fromImage("full_shield.png");
      image.x = 500 + i * 60 - widthOffset;
      image.y = this.isOpponent ? 60 : 450;
      image.width = 100;
      image.height = 130;
      this.addChild(image);
    }

    for (let i = 0; i < this.dices.length; i++) {
      let dice = this.dices[i];
      dice.width = 100;
      dice.height = 100;
      if (dice.inPool) {
        dice.x = 720 - ((i + 1) * 125) - widthOffset;
        dice.y = this.isOpponent ? 525 : 25;
      } else {
        dice.x = 360 + 125 * (i % 2) - widthOffset;
        dice.y = 250 + 125 * Math.floor(i / 2);
      }
      this.addChild(dice);
    }
  }

  realWidth(): number{
    return 420 + 100 * this.upgrades.length;
  }

}

/// <reference path="../reference.ts" />

class CharacterInterface extends PIXI.Container {

  character: CardInterface;

  upgrades: Array<CardInterface> = [];

  dices: Array<DiceInterface>;

  activated: boolean = false;

  damages: number = 0;

  shields: number = 0;

  constructor(character: CharacterView) {
    super();

    this.character = new CardInterface(character);

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
    background.height = 500;
    this.addChild(background);

    for (let i = this.upgrades.length - 1; i >= 0; i--) {
      let upgrade = this.upgrades[i];
      upgrade.x = 300 - (i + 1) * 100;
      upgrade.y = 80;
      upgrade.width = 300;
      upgrade.height = 420;
      this.addChild(upgrade);
    }

    this.character.x = 300;
    this.character.width = 300;
    this.character.height = 420;
    this.addChild(this.character);
  }

}

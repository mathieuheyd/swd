/// <reference path="../reference.ts" />

class CharacterInterface extends PIXI.Container {

  character: CardInterface;

  upgrades: Array<CardInterface>;

  //dices: Array<DiceInterface>;

  activated: boolean = false;

  damageAndShieldsInterface: PIXI.Container;
  damages: number = 0;
  shields: number = 0;

  constructor(character: CharacterView) {
    super();
    this.character = new CardInterface(character);
    this.addChild(this.character);

    let upgradesInterface = new PIXI.Sprite(PIXI.Texture.EMPTY);
    upgradesInterface.width = 210;
    upgradesInterface.height = 100;
    upgradesInterface.y = 100;
    this.addChild(upgradesInterface);

    this.damageAndShieldsInterface = new PIXI.Container();
    this.damageAndShieldsInterface.x = 100;
    this.addChild(this.damageAndShieldsInterface);
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
    this.damageAndShieldsInterface.removeChildren();

    let shieldsInterface = new PIXI.Text('S: ' + this.shields);
    this.damageAndShieldsInterface.addChild(shieldsInterface);

    let damagesInterface = new PIXI.Text('D: ' + this.damages);
    damagesInterface.y = 30;
    this.damageAndShieldsInterface.addChild(damagesInterface);
  }

}

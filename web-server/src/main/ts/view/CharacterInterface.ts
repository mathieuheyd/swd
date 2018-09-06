/// <reference path="../reference.ts" />

class CharacterInterface extends PIXI.Container {

  character: CardInterface;

  damages: number = 0;

  shields: number = 0;

  constructor(character: CharacterView) {
    super();
    this.character = new CardInterface(character);
    this.addChild(this.character);
  }

  updateDamages(amount: number) {
    this.damages += amount;
  }

  updateShields(amount: number) {
    this.shields += amount;
  }

}

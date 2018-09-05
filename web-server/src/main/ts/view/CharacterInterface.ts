/// <reference path="../reference.ts" />

class CharacterInterface extends PIXI.Container {

  character: CardInterface;

  constructor(character: CharacterView) {
    super();
    this.character = new CardInterface(character);
    this.addChild(this.character);
  }

}

/// <reference path="../reference.ts" />

class CharacterInterface extends PIXI.Container {

  constructor(character: CharacterView) {
    super();
    this.addChild(new CardInterface(character.card));
  }

}

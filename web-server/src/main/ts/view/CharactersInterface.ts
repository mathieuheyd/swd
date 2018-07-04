/// <reference path="../reference.ts" />

class CharactersInterface extends PIXI.Container {
  characterInterfaces: Array<CharacterInterface>

  constructor(characters: Array<CharacterView>) {
    super();

    let offset: number = 0;

    for (let c of characters) {
      let characterInterface = new CharacterInterface(c);
      this.addChild(characterInterface);
      characterInterface.x = offset;
      offset += 210;
    }
  }

}
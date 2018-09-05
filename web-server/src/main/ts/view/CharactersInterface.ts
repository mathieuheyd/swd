/// <reference path="../reference.ts" />

class CharactersInterface extends PIXI.Container {
  characterInterfaces: Array<CharacterInterface>

  constructor(characters: Array<CharacterView>) {
    super();

    this.characterInterfaces = [];
    let offset: number = 0;

    for (let c of characters) {
      let characterInterface = new CharacterInterface(c);
      this.characterInterfaces.push(characterInterface);
      this.addChild(characterInterface);
      characterInterface.x = offset;
      offset += 210;
    }
  }

  startAddShields(onAdd: Function) {
    for (let c of this.characterInterfaces) {
      c.character.interactive = true;
      c.character.on('click', () => onAdd(c.character.card.uniqueId));
    }
  }

  stopAddShields() {
    for (let c of this.characterInterfaces) {
      c.character.removeAllListeners();
    }
  }

}
/// <reference path="../reference.ts" />

class CharactersInterface extends PIXI.Container {
  characterInterfaces: Array<CharacterInterface>

  displayRatio: number;

  constructor(characters: Array<CharacterView>, displayRatio: number) {
    super();

    this.displayRatio = displayRatio;

    this.characterInterfaces = [];
    for (let c of characters) {
      let characterInterface = new CharacterInterface(c);
      this.characterInterfaces.push(characterInterface);
    }

    this.updateDisplay();
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

  startActivateCharacter(onActivate: Function) {
    for (let c of this.characterInterfaces) {
      if (!c.activated) {
        c.character.interactive = true;
        c.character.on('click', () => onActivate(c.character.card.uniqueId));
      }
    }
  }

  stopActivateCharacter() {
    for (let c of this.characterInterfaces) {
      c.character.removeAllListeners();
    }
  }

  updateDisplay() {
    this.removeChildren();

    let background = new PIXI.Sprite(PIXI.Texture.WHITE);
    background.width = 100 * this.displayRatio;
    background.height = 100;
    background.tint = 0xFF0000;
    this.addChild(background);

    let charactersWidth = 0;
    for (let characterInterface of this.characterInterfaces) {
      charactersWidth += characterInterface.realWidth();
    }

    let limitingRatio = Math.min(100 * this.displayRatio / charactersWidth, 100 / 650);

    let offset: number = 0;
    for (let characterInterface of this.characterInterfaces) {
      this.addChild(characterInterface);
      characterInterface.x = offset;
      characterInterface.width = characterInterface.realWidth() * limitingRatio;
      characterInterface.height = 650 * limitingRatio;
      offset += characterInterface.realWidth() * limitingRatio;
    }
  }

}
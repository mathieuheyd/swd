/// <reference path="../reference.ts" />

class CardHiddenInterface extends PIXI.Container {

  constructor() {
    super();
    this.addChild(this.cardSprite());
  }

  cardSprite() {
      let url: string = '/client/cardback.png';
      let sprite: PIXI.Sprite = PIXI.Sprite.fromImage(url);
      sprite.width = 70;
      sprite.height = 100;
      return sprite;
    }

}

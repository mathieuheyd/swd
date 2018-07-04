/// <reference path="../reference.ts" />

class CardInterface extends PIXI.Container {

  constructor(card: CardId) {
    super();
    this.addChild(this.cardSprite(card.set, card.id, false));
  }

  cardSprite(cardSet: number, cardId : number, horizontal: Boolean) {
      let set: string = (cardSet.toString() as any).padStart(2, '0');
      let id: string = (cardId.toString() as any).padStart(3, '0');
      let url: string = '/cards/en/' + set + '/' + set + id + '.jpg';
      let sprite: PIXI.Sprite = PIXI.Sprite.fromImage(url);
      if (horizontal) {
        sprite.width = 100;
        sprite.height = 70;
      } else {
        sprite.width = 70;
        sprite.height = 100;
      }
      return sprite;
    }

}

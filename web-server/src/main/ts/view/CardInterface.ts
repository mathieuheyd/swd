/// <reference path="../reference.ts" />

class CardInterface extends PIXI.Container {

  card: CardView;

  horizontal: boolean = false;

  exhausted: boolean = false;

  constructor(card: CardView) {
    super();
    this.card = card;
    this.updateDisplay();
  }

  updateDisplay() {
    this.removeChildren();
    this.addChild(this.cardSprite(this.card.card.set, this.card.card.id, this.horizontal, this.exhausted));
  }

  cardSprite(cardSet: number, cardId : number, horizontal: boolean, exhausted: boolean) {
    let set: string = (cardSet.toString() as any).padStart(2, '0');
    let id: string = (cardId.toString() as any).padStart(3, '0');
    let url: string = '/card/en/' + set + '/' + set + id + '.jpg';
    let sprite: PIXI.Sprite = PIXI.Sprite.fromImage(url);
    if (horizontal) {
      sprite.width = 100;
      sprite.height = 70;
    } else {
      sprite.width = 70;
      sprite.height = 100;
    }
    if (exhausted) {
      sprite.rotation = Math.PI / 2;
      sprite.x = sprite.height;
    }
    return sprite;
  }

}

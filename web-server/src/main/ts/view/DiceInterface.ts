/// <reference path="../reference.ts" />

class DiceInterface extends PIXI.Container {

  dice: DiceView;

  sideId: number = 1;

  constructor(dice: DiceView) {
    super();
    this.dice = dice;
    this.updateDisplay();
  }

  updateDisplay() {
    let background: PIXI.Sprite = new PIXI.Sprite(PIXI.Texture.WHITE);
    background.tint = 0x0000FF;
    background.width = 50;
    background.height = 50;
    this.addChild(background);

    let mask: PIXI.Graphics = new PIXI.Graphics();
    mask.beginFill(0x000000);
    mask.drawCircle(25, 25, 22);
    mask.endFill();
    this.addChild(mask);

    let set: string = (this.dice.card.set.toString() as any).padStart(2, '0');
    let id: string = (this.dice.card.id.toString() as any).padStart(3, '0');
    let url: string = '/cards/en/' + set + '/' + set + id + '.jpg';
    let card: PIXI.Sprite = PIXI.Sprite.fromImage(url);
    card.width = 120;
    card.height = 170;
    card.x = -40;
    card.y = -30;
    this.addChild(card);
    card.mask = mask;
  }

}
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

    let diceFace = new PIXI.Container();
    diceFace.mask = mask;
    this.addChild(diceFace);

    let set: string = (this.dice.card.set.toString() as any).padStart(2, '0');
    let id: string = (this.dice.card.id.toString() as any).padStart(3, '0');
    let url: string = '/cards/en/' + set + '/' + set + id + '.jpg';
    let card: PIXI.Sprite = PIXI.Sprite.fromImage(url);
    card.width = 120;
    card.height = 170;
    card.x = -40;
    card.y = -30;
    diceFace.addChild(card);

    let symbolBackground: PIXI.Graphics = new PIXI.Graphics();
    symbolBackground.beginFill(0x000000);
    symbolBackground.moveTo(50, -8);
    symbolBackground.lineTo(30, 12);
    symbolBackground.lineTo(30, 38);
    symbolBackground.lineTo(50, 58);
    symbolBackground.moveTo(50, -8);
    symbolBackground.endFill();
    diceFace.addChild(symbolBackground);

    let symbol: PIXI.Sprite = PIXI.Sprite.fromImage("dice/indirect.png");
    symbol.x = 32;
    symbol.y = 20;
    symbol.width = 10;
    symbol.height = 10;
    diceFace.addChild(symbol);
  }

}
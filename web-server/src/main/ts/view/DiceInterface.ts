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
    let background: PIXI.Graphics = new PIXI.Graphics();
    background.beginFill(0x0000FF);
    background.moveTo(0, 3);
    background.lineTo(0, 47);
    background.lineTo(3, 50);
    background.lineTo(47, 50);
    background.lineTo(50, 47);
    background.lineTo(50, 3);
    background.lineTo(47, 0);
    background.lineTo(3, 0);
    background.lineTo(0, 3);
    background.endFill();
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
    card.width = 70;
    card.height = 100;
    card.x = -20;
    card.y = -10;
    diceFace.addChild(card);

    let symbolBackground: PIXI.Graphics = new PIXI.Graphics();
    symbolBackground.lineStyle(1, 0xFFFFFF);
    symbolBackground.beginFill(0x000000);
    symbolBackground.moveTo(50, -8);
    symbolBackground.lineTo(30, 12);
    symbolBackground.lineTo(30, 38);
    symbolBackground.lineTo(50, 58);
    symbolBackground.lineTo(50, -8);
    symbolBackground.endFill();
    diceFace.addChild(symbolBackground);

    let symbol: PIXI.Sprite = PIXI.Sprite.fromImage("dice/special.png");
    symbol.x = 30;
    symbol.y = 17;
    symbol.width = 15;
    symbol.height = 15;
    diceFace.addChild(symbol);
    let filter = new PIXI.filters.ColorMatrixFilter();
    filter.negative(false);
    symbol.filters = [filter];

    let resourceBackground: PIXI.Graphics = new PIXI.Graphics();
    resourceBackground.lineStyle(1, 0xFFFFFF);
    resourceBackground.beginFill(0xf4a442);
    resourceBackground.moveTo(21, 39);
    resourceBackground.lineTo(29, 39);
    resourceBackground.lineTo(49, 59);
    resourceBackground.lineTo(1, 59);
    resourceBackground.lineTo(21, 39);
    resourceBackground.endFill();
    diceFace.addChild(resourceBackground);

    let resourceAmount: PIXI.Text = new PIXI.Text('2', {fontSize: 5});
    resourceAmount.x = 21;
    resourceAmount.y = 42;
    diceFace.addChild(resourceAmount);

    let resourceSymbol: PIXI.Sprite = PIXI.Sprite.fromImage("dice/resource.png");
    resourceSymbol.x = 24;
    resourceSymbol.y = 40;
    resourceSymbol.width = 6;
    resourceSymbol.height = 6;
    diceFace.addChild(resourceSymbol);
  }

}
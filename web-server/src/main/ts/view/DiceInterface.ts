/// <reference path="../reference.ts" />

class DiceInterface extends PIXI.Container {

  dice: DiceFullView;

  sideId: number;

  inPool: boolean = false;

  constructor(dice: DiceFullView) {
    super();
    this.dice = dice;
    this.setSide(1);
    this.updateDisplay();
  }

  setSide(sideId: number) {
    this.sideId = sideId;
    this.updateDisplay();
  }

  getSide(): DiceSide {
    let diceDescription = this.dice.description.dice;
    switch (this.sideId) {
      case 1:
        return diceDescription.side1;
      case 2:
        return diceDescription.side2;
      case 3:
        return diceDescription.side3;
      case 4:
        return diceDescription.side4;
      case 5:
        return diceDescription.side5;
      case 6:
        return diceDescription.side6;
    }
    return null;
  }

  updateDisplay() {
    let diceColor: number;
    switch (this.dice.description.color) {
      case CardColor.Red:
        diceColor = 0xe45031;
        break;
      case CardColor.Blue:
        diceColor = 0x5566b3;
        break;
      case CardColor.Yellow:
        diceColor = 0xffc12d;
        break;
      case CardColor.Gray:
        diceColor = 0x97948c;
        break;
    }

    let background: PIXI.Graphics = new PIXI.Graphics();
    background.beginFill(diceColor);
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
    let url: string = '/card/en/' + set + '/' + set + id + '.jpg';
    let card: PIXI.Sprite = PIXI.Sprite.fromImage(url);
    card.width = 70;
    card.height = 100;
    card.x = -20;
    card.y = -10;
    diceFace.addChild(card);

   let diceSide = this.getSide();

    let symbolBgColor = 0x000000;
    if (diceSide.symbol == DiceSymbol.Blank) {
      symbolBgColor = 0xe52c29;
    } else if (diceSide.modifier) {
      symbolBgColor = 0x0e5ca9;
    }
    let symbolBackground: PIXI.Graphics = new PIXI.Graphics();
    symbolBackground.lineStyle(1, 0xFFFFFF);
    symbolBackground.beginFill(symbolBgColor);
    symbolBackground.moveTo(50, -8);
    symbolBackground.lineTo(30, 12);
    symbolBackground.lineTo(30, 38);
    symbolBackground.lineTo(50, 58);
    symbolBackground.lineTo(50, -8);
    symbolBackground.endFill();
    diceFace.addChild(symbolBackground);

    let symbolImage: string;
    switch (diceSide.symbol) {
      case DiceSymbol.MeleeDamage:
        symbolImage = "melee";
        break;
      case DiceSymbol.RangedDamage:
        symbolImage = "ranged";
        break;
      case DiceSymbol.Shield:
        symbolImage = "shield";
        break;
      case DiceSymbol.Resource:
        symbolImage = "resource";
        break;
      case DiceSymbol.Disrupt:
        symbolImage = "disrupt";
        break;
      case DiceSymbol.Discard:
        symbolImage = "discard";
        break;
      case DiceSymbol.Focus:
        symbolImage = "focus";
        break;
      case DiceSymbol.Special:
        symbolImage = "special";
        break;
      case DiceSymbol.Blank:
        symbolImage = "blank";
        break;
    }

    let negativeFilter = new PIXI.filters.ColorMatrixFilter();
    negativeFilter.negative(false);

    if (diceSide.symbol == DiceSymbol.Special || diceSide.symbol == DiceSymbol.Blank) {
      let symbol: PIXI.Sprite = PIXI.Sprite.fromImage("dice/" + symbolImage + ".png");
      symbol.x = 30;
      symbol.y = 17;
      symbol.width = 15;
      symbol.height = 15;
      diceFace.addChild(symbol);
      symbol.filters = [negativeFilter];
    } else {
      let amount: PIXI.Sprite = PIXI.Sprite.fromImage("dice/" + diceSide.value + ".png");
      amount.x = 25;
      amount.y = 8;
      amount.width = 25;
      amount.height = 25;
      diceFace.addChild(amount);
      amount.filters = [negativeFilter];

      if (diceSide.modifier) {
        amount.x = 28;

        let modifier: PIXI.Sprite = PIXI.Sprite.fromImage("dice/8.png");
        modifier.x = 22;
        modifier.y = 8;
        modifier.width = 25;
        modifier.height = 25;
        diceFace.addChild(modifier);
        modifier.filters = [negativeFilter];
      }

      let symbol: PIXI.Sprite = PIXI.Sprite.fromImage("dice/" + symbolImage + ".png");
      symbol.x = 30;
      symbol.y = 24;
      symbol.width = 15;
      symbol.height = 15;
      diceFace.addChild(symbol);
      symbol.filters = [negativeFilter];
    }

    if (diceSide.cost > 0) {
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

      let resourceAmount: PIXI.Sprite = PIXI.Sprite.fromImage("dice/" + diceSide.cost + ".png");
      resourceAmount.x = 15;
      resourceAmount.y = 36;
      resourceAmount.width = 15;
      resourceAmount.height = 15;
      diceFace.addChild(resourceAmount);

      let resourceSymbol: PIXI.Sprite = PIXI.Sprite.fromImage("dice/resource.png");
      resourceSymbol.x = 24;
      resourceSymbol.y = 40;
      resourceSymbol.width = 6;
      resourceSymbol.height = 6;
      diceFace.addChild(resourceSymbol);
    }
  }

}
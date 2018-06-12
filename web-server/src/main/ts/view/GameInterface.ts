/// <reference path="../reference.ts" />

class GameInterface extends PIXI.Container {
  private opponentBattlefield: PIXI.Container = this.addContainer(0, 0, 150, 100, 0x0000bb);
  private opponentHand: PIXI.Container = this.addContainer(250, 0, 300, 100, 0x0000ff);
  private opponentResources: PIXI.Container = this.addContainer(650, 0, 150, 100, 0x0000aa);
  private opponentSupports: PIXI.Container = this.addContainer(0, 100, 150, 200, 0x0000dd);
  private opponentCharacters: PIXI.Container = this.addContainer(150, 100, 500, 200, 0x0000ee);
  private opponentDeck: PIXI.Container = this.addContainer(650, 100, 150, 200, 0x0000cc);

  private playerBattlefield: PIXI.Container = this.addContainer(0, 500, 150, 100, 0xbb0000);
  private playerHand: PIXI.Container = this.addContainer(250, 500, 300, 100, 0xff0000);
  private payerResources: PIXI.Container = this.addContainer(650, 500, 150, 100, 0xaa0000);
  private playerSupports: PIXI.Container = this.addContainer(0, 300, 150, 200, 0xdd0000);
  private playerCharacters: PIXI.Container = this.addContainer(150, 300, 500, 200, 0xee0000);
  private playerDeck: PIXI.Container = this.addContainer(650, 300, 150, 200, 0xcc0000);

  addContainer(x: number, y: number, w: number, h: number, color: number) {
    let container: PIXI.Container = new PIXI.Container();
    container.x = x;
    container.y = y;
    container.width = w;
    container.height = h;
    let background: PIXI.Sprite = new PIXI.Sprite(PIXI.Texture.WHITE);
    background.width = w;
    background.height = h;
    background.tint = color;
    background.alpha = 0.4;
    container.addChild(background);
    this.addChild(container);
    return container;
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
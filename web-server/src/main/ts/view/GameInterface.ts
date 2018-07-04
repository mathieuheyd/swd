/// <reference path="../reference.ts" />

class GameInterface extends PIXI.Container {
  private opponentBattlefield: PIXI.Container = this.addContainer(0, 0, 150, 100, 0x0000bb);
  private opponentHand: PIXI.Container = this.addContainer(250, 0, 300, 100, 0x0000ff);
  private opponentResources: PIXI.Container = this.addContainer(650, 0, 150, 100, 0x0000aa);
  private opponentSupports: PIXI.Container = this.addContainer(0, 100, 150, 200, 0x0000dd);
  opponentCharacters: CharactersInterface;
  private opponentDeck: PIXI.Container = this.addContainer(650, 100, 150, 200, 0x0000cc);

  private playerBattlefield: PIXI.Container = this.addContainer(0, 500, 150, 100, 0xbb0000);
  private playerHand: PIXI.Container = this.addContainer(250, 500, 300, 100, 0xff0000);
  private payerResources: PIXI.Container = this.addContainer(650, 500, 150, 100, 0xaa0000);
  private playerSupports: PIXI.Container = this.addContainer(0, 300, 150, 200, 0xdd0000);
  playerCharacters: CharactersInterface;
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

  setupGame(playerCharacters: Array<CharacterView>, opponentCharacters: Array<CharacterView>) {
    this.playerCharacters = new CharactersInterface(playerCharacters);
    this.playerCharacters.x = 150;
    this.playerCharacters.y = 300;
    this.playerCharacters.width = 500;
    this.playerCharacters.height = 200;
    this.addChild(this.playerCharacters);

    this.opponentCharacters = new CharactersInterface(opponentCharacters);
    this.opponentCharacters.x = 150;
    this.opponentCharacters.y = 100;
    this.opponentCharacters.width = 500;
    this.opponentCharacters.height =  200;
    this.addChild(this.opponentCharacters);
  }

}
/// <reference path="../reference.ts" />

class GameInterface extends PIXI.Container {
  opponentBattlefield: BattlefieldInterface;
  opponentHand: HandInterface;
  private opponentResources: PIXI.Container = this.addContainer(650, 0, 150, 100, 0x0000aa);
  private opponentSupports: PIXI.Container = this.addContainer(0, 100, 150, 200, 0x0000dd);
  opponentCharacters: CharactersInterface;
  private opponentDeck: PIXI.Container = this.addContainer(650, 100, 150, 200, 0x0000cc);

  playerBattlefield: BattlefieldInterface;
  playerHand: HandInterface;
  //private payerResources: PIXI.Container = this.addContainer(650, 500, 150, 100, 0xaa0000);
  playerActions: ActionInterface;
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

  setupGame(playerCharacters: Array<CharacterView>, playerBattlefield: CardView,
            opponentCharacters: Array<CharacterView>, opponentBattlefield: CardView) {
    this.playerCharacters = new CharactersInterface(playerCharacters);
    this.playerCharacters.x = 150;
    this.playerCharacters.y = 300;
    this.playerCharacters.width = 500;
    this.playerCharacters.height = 200;
    this.addChild(this.playerCharacters);

    this.playerBattlefield = new BattlefieldInterface(playerBattlefield);
    this.playerBattlefield.x = 0;
    this.playerBattlefield.y = 500;
    this.playerBattlefield.width = 150;
    this.playerBattlefield.height = 100;
    this.addChild(this.playerBattlefield);

    this.playerHand = new HandInterface();
    this.playerHand.x = 250;
    this.playerHand.y = 500;
    this.playerHand.width = 300;
    this.playerHand.height = 100;
    this.addChild(this.playerHand);

    this.playerActions = new ActionInterface();
    this.playerActions.x = 650;
    this.playerActions.y = 500;
    this.playerActions.width = 150;
    this.playerActions.height = 100;
    this.addChild(this.playerActions);

    this.opponentCharacters = new CharactersInterface(opponentCharacters);
    this.opponentCharacters.x = 150;
    this.opponentCharacters.y = 100;
    this.opponentCharacters.width = 500;
    this.opponentCharacters.height =  200;
    this.addChild(this.opponentCharacters);

    this.opponentBattlefield = new BattlefieldInterface(opponentBattlefield);
    this.opponentBattlefield.x = 0;
    this.opponentBattlefield.y = 0;
    this.opponentBattlefield.width = 150;
    this.opponentBattlefield.height = 100;
    this.addChild(this.opponentBattlefield);

    this.opponentHand = new HandInterface();
    this.opponentHand.x = 250;
    this.opponentHand.y = 0;
    this.opponentHand.width = 300;
    this.opponentHand.height = 100;
    this.addChild(this.opponentHand);
  }

}
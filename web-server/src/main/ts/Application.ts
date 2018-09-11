/// <reference path="reference.ts" />

class Application {
  userName: string;
  view: PIXI.Application;

  constructor(userName: string) {
    this.userName = userName;
    this.view = new PIXI.Application(800, 600, {backgroundColor : 0x1099bb});
    document.body.appendChild(this.view.view);
  }

  findGame() {
    new MatchMaking(this.userName, (gameId: string) => {
      let gameView = new GameInterface();
      this.view.stage.addChild(gameView);
      new Game(gameId, gameView);
    });
  }
}

function startApp() {
  // For testing purposes, automatically launch a game
  let userName = new URL(window.location.href).searchParams.get("user");
  let app = new Application(userName);
  app.findGame();
}
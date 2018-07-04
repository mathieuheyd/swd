/// <reference path="reference.ts" />

class Game {
  socket: WebSocket;
  view: GameInterface;
  board: BoardState;

  constructor(gameId: string, gameView: GameInterface) {
    this.view = gameView;

    this.socket = new WebSocket("ws://localhost:8080/game/" + gameId);
    this.socket.onopen = (event: Event) => {
      console.log('Connected to Game');
    }
    this.socket.onclose = (event: CloseEvent) => {
      console.log('Connection to Game closed');
    }
    this.socket.onmessage = (event: MessageEvent) => {
      console.log('New message', event.data);
      let jsonObj = JSON.parse(event.data);
      let className = Object.getOwnPropertyNames(jsonObj)[0];
      let instance = Object.create((window as any)[className].prototype);
      instance.constructor.apply(instance);
      for (let propName in jsonObj[className]) {
        instance[propName] = jsonObj[className][propName]
      }
      this.handleMessage(instance);
    }
  }

  handleMessage(message: EventView) {
    /*
    if (message instanceof SetupView) {
      console.log('SetupView', message);
      let setupView: SetupView = message;
      this.view.setupCharacters(setupView.player.characters, setupView.opponent.characters);
    } else if (message instanceof DrawStartingHandView) {
      console.log('DrawStartingHandView', message);
    } else {
      console.log('Unknown EventView', message);

    }
    */
    console.log('new EventView', message);
    message.updateInterface(this.view);
  }
}

class CharacterView {
  uniqueId: Number;
  card: CardId;
  dices: Array<DiceView>;
}
class DiceView {
  uniqueId: Number;
  card: CardId;
}
class CardView {
  uniqueId: Number;
  card: CardId;
}
class PlayerSetupView {
  characters: Array<CharacterView>;
  battlefield: CardView;
  deckSize: Number;
}

interface EventView {
  updateInterface(view: GameInterface): void;
}
class SetupView implements EventView {
  player: PlayerSetupView;
  opponent: PlayerSetupView;

  updateInterface(view: GameInterface) {
    view.setupGame(this.player.characters, this.opponent.characters);
  }
}
class DrawStartingHandView implements EventView {
  player: Array<CardView>;
  opponent: Number;

  updateInterface(view: GameInterface) {
  }
}
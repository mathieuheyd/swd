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
    console.log('new EventView', message);
    message.updateInterface(this);
  }

  startMulligan() {
    this.view.playerHand.startMulligan();
    this.view.playerActions.mulliganAction(this.mulligan);
  }

  mulligan = () => {
    console.log('Mulligan', this.view.playerHand.cardsToMulligan);
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
  updateInterface(game: Game): void;
}
class SetupView implements EventView {
  player: PlayerSetupView;
  opponent: PlayerSetupView;

  updateInterface(game: Game) {
    game.view.setupGame(this.player.characters, this.player.battlefield, this.opponent.characters, this.opponent.battlefield);
  }
}
class DrawStartingHandView implements EventView {
  player: Array<CardView>;
  opponent: Number;

  updateInterface(game: Game) {
    for (let c of this.player) {
      game.view.playerHand.addCard(c);
    }
    game.startMulligan();
  }
}
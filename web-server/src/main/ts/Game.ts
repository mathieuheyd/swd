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
      //console.log('New message', event.data);
      let jsonObj = JSON.parse(event.data);
      let instance = this.buildObject(jsonObj);
      this.handleMessage(instance);
    }
  }

  private buildObject(jsonObj: any) {
    let properties = Object.getOwnPropertyNames(jsonObj);
    if (properties.length == 1 && typeof((window as any)[properties[0]]) === "function") {
      let className = properties[0];
      let instance = Object.create((window as any)[className].prototype);
      instance.constructor.apply(instance);
      for (let propName in jsonObj[className]) {
        if (Array.isArray(jsonObj[className][propName])) {
          instance[propName] = jsonObj[className][propName].map((value: any) => this.buildObject(value));
        } else {
          instance[propName] = this.buildObject(jsonObj[className][propName]);
        }
      }
      return instance;
    } else {
      return jsonObj;
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

  stopMulligan() {
    this.view.playerHand.stopMulligan();
    this.view.playerActions.noAction();
  }

  mulligan = () => {
    let cards = this.view.playerHand.cardsToMulligan;
    this.stopMulligan();
    let message = { ActionUserMessage: {
      action: {
        MulliganAction: {
          cards: Array.from(cards)
        }
      }
    }};
    this.socket.send(JSON.stringify(message));
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
class PlayerInfoView {
  name: String;
}
class PlayerSetupView {
  characters: Array<CharacterView>;
  battlefield: CardView;
  deckSize: Number;
}
class DiceRollView {
  dice: DiceView;
  side: Number;
}

interface EventView {
  updateInterface(game: Game): void;
}
class GameInfoView implements EventView {
  player: PlayerInfoView;
  opponent: PlayerInfoView;

  updateInterface(game: Game) {

  }
}
class SetupView implements EventView {
  player: PlayerSetupView;
  opponent: PlayerSetupView;

  updateInterface(game: Game) {
    game.view.setupGame(this.player.characters, this.player.battlefield, this.opponent.characters, this.opponent.battlefield);
  }
}
class ActionView implements EventView {
  player: Boolean;
  //action: GameAction;
  effects: Array<EffectView>;

  updateInterface(game: Game) {
    console.log(this.effects);
    for (let effect of this.effects) {
      effect.updateInterface(game);
    }
  }
}

interface EffectView {
  updateInterface(game: Game): void;
}
class DrawStartingHandView implements EffectView {
  cards: Array<CardView>;

  updateInterface(game: Game) {
    for (let c of this.cards) {
      game.view.playerHand.addCard(c);
    }
    game.startMulligan();
  }
}
class DrawStartingHandOpponentView implements EffectView {
  cards: Number;

  updateInterface(game: Game) {
    for (let i = 0; i < this.cards; i++) {
      game.view.opponentHand.addCard();
    }
  }
}
class MulliganView implements EffectView {
  mulliganCards: Array<CardView>;
  drawnCards: Array<CardView>;

  updateInterface(game: Game) {
    for (let c of this.mulliganCards) {
      game.view.playerHand.removeCard(c);
    }
    for (let c of this.drawnCards) {
      game.view.playerHand.addCard(c);
    }
  }
}
class MulliganOpponentView implements EffectView {
  mulliganCards: Number;
  drawnCards: Number;

  updateInterface(game: Game) {
    for (let i = 0; i < this.mulliganCards; i++) {
      game.view.opponentHand.removeCard();
    }
    for (let i = 0; i < this.drawnCards; i++) {
      game.view.opponentHand.addCard();
    }
  }
}
class TossView implements EffectView {
  dices: Array<DiceRollView>;
  total: Number;

  updateInterface(game: Game) {

  }
}
class ChooseBattlefieldView implements EffectView {
  battlefield: CardView;

  updateInterface(game: Game) {

  }
}
class ShieldAddedView implements EffectView {
  character: CardView;
  amount: Number;

  updateInterface(game: Game) {

  }
}
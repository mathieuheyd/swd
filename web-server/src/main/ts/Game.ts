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

  startChooseBattlefield() {
    this.view.playerActions.chooseBattlefieldAction();
    this.view.playerBattlefield.startChoose(this.chooseBattlefield);
    this.view.opponentBattlefield.startChoose(this.chooseBattlefield);
  }

  stopChooseBattlefield() {
    this.view.playerActions.noAction();
    this.view.playerBattlefield.stopChoose();
    this.view.opponentBattlefield.stopChoose();
  }

  chooseBattlefield = (cardId: Number) => {
    this.stopChooseBattlefield();
    let message = { ActionUserMessage: {
      action: {
        ChooseBattlefield: {
          card: cardId
        }
      }
    }};
    this.socket.send(JSON.stringify(message));
  }

  startAddShields() {
    this.view.playerActions.addShieldsAction();
    this.view.playerCharacters.startAddShields(this.addShields);
  }

  stopAddShields() {
    this.view.playerActions.noAction();
    this.view.playerCharacters.stopAddShields();
  }

  addShields = (uniqueId: Number) => {
    this.stopAddShields();
    let message = { ActionUserMessage: {
      action: {
        AddShield: {
          card: uniqueId
        }
      }
    }};
    this.socket.send(JSON.stringify(message));
  }

  startAction() {
    this.view.playerActions.passAction(this.pass);
  }

  stopAction() {
    this.view.playerActions.noAction();
  }

  pass = () => {
    this.stopAction();
    let message = { ActionUserMessage: {
      action: {
        PassAction: {}
      }
    }};
    this.socket.send(JSON.stringify(message));
  }

}

class CharacterView {
  uniqueId: number;
  card: CardId;
  dices: Array<DiceView>;
}
class DiceView {
  uniqueId: number;
  card: CardId;
}
class CardView {
  uniqueId: number;
  card: CardId;
}
class PlayerInfoView {
  name: string;
}
class PlayerSetupView {
  characters: Array<CharacterView>;
  battlefield: CardView;
  deckSize: number;
}
class DiceRollView {
  dice: DiceView;
  side: number;
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
  player: boolean;
  //action: GameAction;
  effects: Array<EffectView>;

  updateInterface(game: Game) {
    console.log(this.effects);
    for (let effect of this.effects) {
      effect.updateInterface(game);
    }
  }
}
class ActionRequiredView implements EventView {
  player: boolean;
  action: string;

  updateInterface(game: Game) {
    if (this.player) {
      console.log('Action Required' + this.action);
      if (this.action == 'Mulligan') {
        game.startMulligan();
      } else if (this.action == 'ChooseBattlefield') {
        game.startChooseBattlefield();
      } else if (this.action == 'AddShields') {
        game.startAddShields();
      }
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
  }
}
class DrawStartingHandOpponentView implements EffectView {
  cards: number;

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
  mulliganCards: number;
  drawnCards: number;

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
  total: number;

  updateInterface(game: Game) {

  }
}
class ChooseBattlefieldView implements EffectView {
  player: boolean;

  updateInterface(game: Game) {
    if (this.player) {
      game.view.opponentBattlefield.setCard(null);
    } else {
      game.view.playerBattlefield.setCard(null);
    }
  }
}
class ShieldAddedView implements EffectView {
  character: CardView;
  amount: number;

  updateInterface(game: Game) {
    let character = game.view.playerCharacters.characterInterfaces
      .concat(game.view.opponentCharacters.characterInterfaces)
      .filter(character => character.character.card.uniqueId == this.character.uniqueId)[0];
    character.updateShields(this.amount);
  }
}
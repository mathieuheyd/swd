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

  addShields = (uniqueId: number) => {
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
    this.view.playerCharacters.startActivateCharacter(this.activateCharacter);
    this.view.playerBattlefield.startClaim(this.claimBattlefield);
    this.view.opponentBattlefield.startClaim(this.claimBattlefield);
  }

  stopAction() {
    this.view.playerActions.noAction();
    this.view.playerCharacters.stopActivateCharacter();
    this.view.playerBattlefield.stopClaim();
    this.view.opponentBattlefield.stopClaim();
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

  activateCharacter = (uniqueId: number) => {
    this.stopAction();
    let message = { ActionUserMessage: {
      action: {
        ActivateAction: {
          card: uniqueId
        }
      }
    }};
    this.socket.send(JSON.stringify(message));
  }

  claimBattlefield = () => {
    this.stopAction();
    let message = { ActionUserMessage: {
      action: {
        ClaimBattlefield: {}
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

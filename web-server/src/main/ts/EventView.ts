/// <reference path="reference.ts" />

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
      } else if (this.action == 'Action') {
        game.startAction();
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
class ResourceAddedView implements EffectView {
  player: boolean;
  amount: number;

  updateInterface(game: Game) {
    if (this.player) {
      game.view.playerResources.addResources(this.amount);
    } else {
      game.view.opponentResources.addResources(this.amount);
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
class CharacterActivatedView implements EffectView {
  character: CardView;

  updateInterface(game: Game) {
    let character = game.view.playerCharacters.characterInterfaces
      .concat(game.view.opponentCharacters.characterInterfaces)
      .filter(character => character.character.card.uniqueId == this.character.uniqueId)[0];
    character.activateCharacter();
  }
}
class DiceInPoolView implements EffectView {
  dice: DiceView;
  side: number;

  updateInterface(game: Game) {

  }
}
class BattlefieldClaimedView implements EffectView {
  player: boolean;

  updateInterface(game: Game) {
    let battlefield = game.view.opponentBattlefield.card || game.view.playerBattlefield.card;
    if (this.player) {
      game.view.opponentBattlefield.setCard(null);
      game.view.playerBattlefield.setCard(battlefield);
      game.view.playerBattlefield.claimed = true;
    } else {
      game.view.playerBattlefield.setCard(null);
      game.view.opponentBattlefield.setCard(battlefield);
      game.view.opponentBattlefield.claimed = true;
    }
  }
}
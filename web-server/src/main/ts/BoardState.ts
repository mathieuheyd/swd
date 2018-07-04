/// <reference path="reference.ts" />

class BoardState {
  characters: UniqueCard[];
  hand: UniqueCard[];

  constructor(characters: UniqueCard[], hand: UniqueCard[]) {
    this.characters = characters;
    this.hand = hand;
  }
}

class UniqueCard {
  card: CardId;
  uniqueId: number;

  constructor(card: CardId, uniqueId: number) {
    this.card = card;
    this.uniqueId = uniqueId;
  }
}

class CardId {
  set: number;
  id: number;
}

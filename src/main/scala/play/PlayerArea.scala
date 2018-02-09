package play

import entities.{Card, Character, Dice, DiceSide}

class InPlayCharacter(val uniqueId: Int, val character: Character, val dices: Array[InPlayDice]) {}

class InPlayDice(val uniqueId: Int, val dice: Dice) {

  def roll() = {}

  def currentSide: DiceSide = {
    dice.side1
  }

}

case class InPlayCard(uniqueId: Int, card: Card, dice: Option[InPlayDice])

class Deck(var cards: Seq[InPlayCard]) {
  def shuffle() = {

  }
  def draw(): InPlayCard = {
    val card = cards.head
    cards = cards.tail
    card
  }
  def addCard(card: InPlayCard) = {
    cards = cards :+ card
  }
}

class DiscardPile(val cards: Array[Card]) {}

class PlayerArea(val characters: Array[InPlayCharacter], val deck: Deck) {

  var hand: Seq[InPlayCard] = Seq.empty

  def shuffleDeck() = deck.shuffle()

  def drawCards(numberOfCards: Int): Seq[InPlayCard] = {
    (1 to numberOfCards).map(_ => deck.draw())
  }

  def putCardInDeck(card: InPlayCard) = {
    deck.addCard(card)
  }

  def putCardsInHand(cards: Seq[InPlayCard]) = {
    hand = hand ++ cards
  }

  def getCardInHand(uniqueId: Int): Option[InPlayCard] = {
    hand.find(c => c.uniqueId == uniqueId)
  }

  def popCardFromHand(uniqueId: Int): Option[InPlayCard] = {
    val card = getCardInHand(uniqueId)
    hand = hand.filterNot(c => c.uniqueId == uniqueId)
    card
  }

  def getCharacterOrSupport(uniqueId: Int): Option[InPlayCharacter] = {
    characters.find(c => c.uniqueId == uniqueId)
  }

}

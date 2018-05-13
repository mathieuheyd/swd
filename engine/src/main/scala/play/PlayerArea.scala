package play

import entities.{Battlefield, Card, Character, Dice, DiceSide}

import scala.util.Random

class InPlayCharacter(val uniqueId: Int, val character: Character, val dices: Array[InPlayDice]) {
  var isActivated = false
  var health = character.maxHealth
  var shields = 0
}

class InPlayDice(val uniqueId: Int, val dice: Dice) {

  var sideId = 1
  var inPool = false

  def roll() = {
    sideId = scala.util.Random.nextInt(6) + 1
  }

  def currentSide: DiceSide = {
    sideId match {
      case 1 => dice.side1
      case 2 => dice.side2
      case 3 => dice.side3
      case 4 => dice.side4
      case 5 => dice.side5
      case 6 => dice.side6
    }
  }

}

case class InPlayCard(uniqueId: Int, card: Card, dice: Option[InPlayDice])

class Deck(var cards: Seq[InPlayCard]) {
  def shuffle() = {
    cards = Random.shuffle(cards)
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

case class InPlayBattlefield(uniqueId: Int, card: Battlefield)

class DiscardPile(val cards: Array[Card]) {}

class PlayerArea(val player: Player.Value,
                 val characters: Array[InPlayCharacter],
                 val deck: Deck,
                 var battlefield: Option[InPlayBattlefield]) {

  var battlefieldClaimed = false

  var resources = 0
  var hand: Seq[InPlayCard] = Seq.empty
  var discardPile: Seq[InPlayCard] = Seq.empty

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

  def putCardInDiscardPile(card: InPlayCard) = {
    discardPile = discardPile :+ card
  }

  def getCardInHand(uniqueId: Int): Option[InPlayCard] = {
    hand.find(c => c.uniqueId == uniqueId)
  }

  def popCardFromHand(uniqueId: Int): Option[InPlayCard] = {
    val card = getCardInHand(uniqueId)
    hand = hand.filterNot(c => c.uniqueId == uniqueId)
    card
  }

  def getRandomCardFromHand(): Option[InPlayCard] = {
    if (hand.size > 0) {
      Some(hand(scala.util.Random.nextInt(hand.size)))
    } else
      None
  }

  def getCharacterOrSupport(uniqueId: Int): Option[InPlayCharacter] = {
    characters.find(c => c.uniqueId == uniqueId)
  }

  def getDice(uniqueId: Int): Option[InPlayDice] = {
    characters.flatMap(c => c.dices).find(d => d.uniqueId == uniqueId)
  }

}

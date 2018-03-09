package entities

case class DeckCharacter(val character: Character, val elite: Boolean)

case class FullDeck(val characters: Array[DeckCharacter], val deck: Array[DeckCard], val battlefield: Battlefield) {

  def isValid(): Boolean = {
    if (characters.isEmpty)
      return false

    if (characters.exists(c => c.elite && !c.character.canElite))
      return false

    val totalPoints = characters.map { c => c.character.points(c.elite) }.sum
    if (totalPoints > 30)
      return false

    if (characters.exists(_.character.affiliation == Affiliation.Hero) && characters.exists(_.character.affiliation == Affiliation.Villain))
      return false

    val uniqueCharacters = characters.filter(_.character.unique)
    if (uniqueCharacters.map(_.character.title).toSeq.size != uniqueCharacters.size)
      return false

    if (deck.size != 30)
      return false

    if (deck.exists(c => c.cardType != CardType.Event && c.cardType != CardType.Support && c.cardType != CardType.Upgrade))
      return false

    if (deck.groupBy(identity).exists(_._2.size > 2))
      return false

    val charactersAffiliations = characters.map(_.character.affiliation).toSet
    if (deck.exists(c => c.color != CardColor.Gray && !charactersAffiliations.contains(c.affiliation)))
      return false

    if (battlefield.cardType != CardType.Battlefield)
      return false

    return true
  }

}

package entities

import org.scalatest.FlatSpec

class CharacterSpec extends FlatSpec {

  "A character with elite points" should "be eligible to elite" in {
    val points = CharacterPoints(10, Some(15))
    val character = new Character(CardId(CardSet.Awakenings, 1), "", None, Affiliation.Neutral, CardColor.Gray, true, CardRarity.Common, None, points, 10)
    assert(character.canElite == true)
  }

  "A character with no elite points" should "not be eligible to elite" in {
    val points = CharacterPoints(10, None)
    val character = new Character(CardId(CardSet.Awakenings, 1), "", None, Affiliation.Neutral, CardColor.Gray, true, CardRarity.Common, None, points, 10)
    assert(character.canElite == false)
  }

  "A character" should "cost the right amount of points" in {
    val points = CharacterPoints(10, None)
    val character = new Character(CardId(CardSet.Awakenings, 1), "", None, Affiliation.Neutral, CardColor.Gray, true, CardRarity.Common, None, points, 10)
    assert(character.points(false) == 10)
  }

  "A character with elite points" should "cost the right amount when played in elite or not" in {
    val points = CharacterPoints(10, Some(15))
    val character = new Character(CardId(CardSet.Awakenings, 1), "", None, Affiliation.Neutral, CardColor.Gray, true, CardRarity.Common, None, points, 10)
    assert(character.points(false) == 10)
    assert(character.points(true) == 15)
  }



}

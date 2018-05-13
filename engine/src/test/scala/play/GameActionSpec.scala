package play

import collection.Awakenings
import entities.{Dice, DiceSide, DiceSideSymbol}
import org.scalatest.FlatSpec
import play.history._

object FakeData {

  def diceOneSide(uniqueId: Int,
                      symbol: DiceSideSymbol.Value,
                      value: Int = 1,
                      modifier: Boolean = false,
                      cost: Int = 0,
                      inPool: Boolean = false) = {
    val dice = new InPlayDice(uniqueId, Dice(
      DiceSide(symbol, value, modifier, cost),
      DiceSide(DiceSideSymbol.Blank),
      DiceSide(DiceSideSymbol.Blank),
      DiceSide(DiceSideSymbol.Blank),
      DiceSide(DiceSideSymbol.Blank),
      DiceSide(DiceSideSymbol.Blank)
    ))
    dice.inPool = inPool
    dice.sideId = 1
    dice
  }

}

class GameActionSpec extends FlatSpec {

  "Activate Action" should "not be possible if character is already activated" in {
    val action = ActivateAction(1)

    val character = new InPlayCharacter(1, Awakenings._1, Array.empty)
    character.isActivated = true
    val playerArea = new PlayerArea(Player.Player1, Array(character), new Deck(Seq.empty), None)

    val opponentArea = new PlayerArea(Player.Player2, Array.empty, new Deck(Seq.empty), None)

    assert(action.isValid(Player.Player1, playerArea, opponentArea, new GameHistory) == false)
  }

  "Activate Action" should "activate character and roll dices" in {
    val action = ActivateAction(1)

    val playerArea = new PlayerArea(
      Player.Player1,
      Array(new InPlayCharacter(
        1,
        Awakenings._1,
        Array(new InPlayDice(2, Awakenings._1.dice.get)))),
      new Deck(Seq.empty),
      None)

    val opponentArea = new PlayerArea(Player.Player2, Array.empty, new Deck(Seq.empty), None)

    assert(action.isValid(Player.Player1, playerArea, opponentArea, new GameHistory) == true)

    assert(action.process(Player.Player1, playerArea, opponentArea, new GameHistory) ==
      HistoryEvent(Player.Player1, action, Seq(CharacterActivatedEffect(1), DiceInPoolEffect(2), DiceRolledEffect(2, 1))))
  }

  "Resolve a non-existing dice" should "not be valid" in {
    val action = ResolveDices(List(3), List.empty)

    val playerArea = new PlayerArea(
      Player.Player1,
      Array(new InPlayCharacter(
        1,
        Awakenings._1,
        Array(new InPlayDice(2, Awakenings._1.dice.get)))),
      new Deck(Seq.empty),
      None)

    val opponentArea = new PlayerArea(Player.Player2, Array.empty, new Deck(Seq.empty), None)

    assert(action.isValid(Player.Player1, playerArea, opponentArea, new GameHistory) == false)
  }

  "Resolve a dice not in pool" should "not be valid" in {
    val action = ResolveDices(List(2), List.empty)

    val playerArea = new PlayerArea(
      Player.Player1,
      Array(new InPlayCharacter(
        1,
        Awakenings._1,
        Array(FakeData.diceOneSide(2, DiceSideSymbol.Resource, inPool = false)))),
      new Deck(Seq.empty),
      None)

    val opponentArea = new PlayerArea(Player.Player2, Array.empty, new Deck(Seq.empty), None)

    assert(action.isValid(Player.Player1, playerArea, opponentArea, new GameHistory) == false)
  }

  "Resolve different dice symbols" should "not be valid" in {
    val action = ResolveDices(List(2, 3), List.empty)

    val playerArea = new PlayerArea(
      Player.Player1,
      Array(new InPlayCharacter(
        1,
        Awakenings._1,
        Array(
          FakeData.diceOneSide(2, DiceSideSymbol.Resource, inPool = true),
          FakeData.diceOneSide(3, DiceSideSymbol.Disrupt, inPool = true)))),
      new Deck(Seq.empty),
      None)

    val opponentArea = new PlayerArea(Player.Player2, Array.empty, new Deck(Seq.empty), None)

    assert(action.isValid(Player.Player1, playerArea, opponentArea, new GameHistory) == false)
  }

  "Resolve only modified dices" should "not be valid" in {
    val action = ResolveDices(List(2, 3), List.empty)

    val playerArea = new PlayerArea(
      Player.Player1,
      Array(new InPlayCharacter(
        1,
        Awakenings._1,
        Array(
          FakeData.diceOneSide(2, DiceSideSymbol.Resource, inPool = true, modifier = true),
          FakeData.diceOneSide(3, DiceSideSymbol.Resource, inPool = true, modifier = true)))),
      new Deck(Seq.empty),
      None)

    val opponentArea = new PlayerArea(Player.Player2, Array.empty, new Deck(Seq.empty), None)

    assert(action.isValid(Player.Player1, playerArea, opponentArea, new GameHistory) == false)
  }

  "Resolve dices with cost" should "remove resources from the player" in {
    val action = ResolveDices(List(2, 3), List.empty)

    val playerArea = new PlayerArea(
      Player.Player1,
      Array(new InPlayCharacter(
        1,
        Awakenings._1,
        Array(
          FakeData.diceOneSide(2, DiceSideSymbol.Disrupt, cost = 1, inPool = true),
          FakeData.diceOneSide(3, DiceSideSymbol.Disrupt, cost = 2, inPool = true)))),
      new Deck(Seq.empty),
      None)
    playerArea.resources = 4

    val opponentArea = new PlayerArea(Player.Player2, Array.empty, new Deck(Seq.empty), None)

    assert(action.isValid(Player.Player1, playerArea, opponentArea, new GameHistory) == true)
    action.process(Player.Player1, playerArea, opponentArea, new GameHistory)

    assert(playerArea.resources == 1)
  }

  "Resolve a resource dice" should "add resource to the player" in {
    val action = ResolveDices(List(2), List.empty)

    val playerArea = new PlayerArea(
      Player.Player1,
      Array(new InPlayCharacter(
        1,
        Awakenings._1,
        Array(FakeData.diceOneSide(2, DiceSideSymbol.Resource, value = 3, inPool = true)))),
      new Deck(Seq.empty),
      None)
    playerArea.resources = 1

    val opponentArea = new PlayerArea(Player.Player2, Array.empty, new Deck(Seq.empty), None)

    assert(action.isValid(Player.Player1, playerArea, opponentArea, new GameHistory) == true)
    action.process(Player.Player1, playerArea, opponentArea, new GameHistory)

    assert(playerArea.resources == 4)
  }

}

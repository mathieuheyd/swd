package collection

import entities._

object TwoPlayerGame {

  val _1 = Character(
    CardId(CardSet.TwoPlayersGame, 1),
    "Kylo Ren",
    Some("Tormented One"),
    Affiliation.Villain,
    CardColor.Blue,
    true,
    CardRarity.Fixed,
    Some(Dice(
      DiceSide(DiceSideSymbol.MeleeDamage, 1),
      DiceSide(DiceSideSymbol.MeleeDamage, 2),
      DiceSide(DiceSideSymbol.MeleeDamage, 2, false, 1),
      DiceSide(DiceSideSymbol.Disrupt, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Blank)
    )),
    CharacterPoints(14, Some(17)),
    12
  )

  val _2 = Character(
    CardId(CardSet.TwoPlayersGame, 2),
    "Captain Phasma",
    Some("Ruthless Tactician"),
    Affiliation.Villain,
    CardColor.Red,
    true,
    CardRarity.Fixed,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 1),
      DiceSide(DiceSideSymbol.RangedDamage, 2),
      DiceSide(DiceSideSymbol.Focus, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    CharacterPoints(10, Some(14)),
    10
  )

  val _3 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 3),
    "Droid Commandos",
    Affiliation.Villain,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Support,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 1),
      DiceSide(DiceSideSymbol.RangedDamage, 2),
      DiceSide(DiceSideSymbol.Focus, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    4
  )

  val _4 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 4),
    "Captain Phasma's Blaster",
    Affiliation.Villain,
    CardColor.Red,
    true,
    CardRarity.Fixed,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 1),
      DiceSide(DiceSideSymbol.RangedDamage, 2),
      DiceSide(DiceSideSymbol.RangedDamage, 3, false, 1),
      DiceSide(DiceSideSymbol.RangedDamage, 3, true),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Blank)
    )),
    3
  )

  val _5 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 5),
    "Pretorian Guard",
    Affiliation.Villain,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.MeleeDamage, 1),
      DiceSide(DiceSideSymbol.MeleeDamage, 2),
      DiceSide(DiceSideSymbol.MeleeDamage, 2, true),
      DiceSide(DiceSideSymbol.Shield, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Blank)
    )),
    2
  )

  val _6 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 6),
    "Dark Counset",
    Affiliation.Villain,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.Focus, 1),
      DiceSide(DiceSideSymbol.Focus, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank),
      DiceSide(DiceSideSymbol.Blank)
    )),
    1
  )

  val _7 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 7),
    "Crossguard Lightsaber",
    Affiliation.Villain,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.MeleeDamage, 1),
      DiceSide(DiceSideSymbol.MeleeDamage, 2, true),
      DiceSide(DiceSideSymbol.Shield, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    2
  )

  val _8 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 8),
    "Force Stasis",
    Affiliation.Villain,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.Disrupt, 1),
      DiceSide(DiceSideSymbol.Shield, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    4
  )

  val _9 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 9),
    "Jedi Rival",
    Affiliation.Villain,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Upgrade,
    None,
    1
  )

  val _10 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 10),
    "Armored Support",
    Affiliation.Villain,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Support,
    None,
    1
  )

  val _11 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 11),
    "First Strike",
    Affiliation.Villain,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    2
  )

  val _12 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 12),
    "I Have You Now",
    Affiliation.Neutral,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    1
  )

  val _13 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 13),
    "Imperial Envoy",
    Affiliation.Villain,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    2
  )

  val _14 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 14),
    "Mobilize",
    Affiliation.Neutral,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    3
  )

  val _15 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 15),
    "Tactital Mastery",
    Affiliation.Villain,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    1
  )

  val _16 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 16),
    "As I Have Foreseen",
    Affiliation.Neutral,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    0
  )

  val _17 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 17),
    "Clash",
    Affiliation.Neutral,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    1
  )

  val _18 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 18),
    "Intimidate",
    Affiliation.Villain,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    0
  )

  val _19 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 19),
    "Your Skills Are Complete",
    Affiliation.Villain,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    0
  )

  val _20 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 20),
    "Doubt",
    Affiliation.Villain,
    CardColor.Gray,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    0
  )

  val _21 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 21),
    "Parry",
    Affiliation.Neutral,
    CardColor.Gray,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    1
  )

  val _22 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 22),
    "Subjugate",
    Affiliation.Villain,
    CardColor.Gray,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    2
  )

  val _23 = Battlefield(
    CardId(CardSet.TwoPlayersGame, 23),
    "Hangar Bay",
    Some("Imperial Fleet"),
    CardRarity.Fixed
  )

  val _24 = Character(
    CardId(CardSet.TwoPlayersGame, 24),
    "Rey",
    Some("Finding The Ways"),
    Affiliation.Hero,
    CardColor.Blue,
    true,
    CardRarity.Fixed,
    Some(Dice(
      DiceSide(DiceSideSymbol.MeleeDamage, 1),
      DiceSide(DiceSideSymbol.MeleeDamage, 2),
      DiceSide(DiceSideSymbol.Discard, 1),
      DiceSide(DiceSideSymbol.Shield, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Blank)
    )),
    CharacterPoints(12, Some(15)),
    11
  )

  val _25 = Character(
    CardId(CardSet.TwoPlayersGame, 25),
    "Poe Dameron",
    Some("More Than A Pilot"),
    Affiliation.Hero,
    CardColor.Red,
    true,
    CardRarity.Fixed,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 1),
      DiceSide(DiceSideSymbol.RangedDamage, 2),
      DiceSide(DiceSideSymbol.Focus, 2),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    CharacterPoints(14, Some(17)),
    12
  )

  val _26 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 26),
    "Medical Droid",
    Affiliation.Hero,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Support,
    Some(Dice(
      DiceSide(DiceSideSymbol.Shield, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank),
      DiceSide(DiceSideSymbol.Blank)
    )),
    1
  )

  val _27 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 27),
    "Strike Team",
    Affiliation.Hero,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Support,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 2),
      DiceSide(DiceSideSymbol.RangedDamage, 3),
      DiceSide(DiceSideSymbol.RangedDamage, 3, false, 1),
      DiceSide(DiceSideSymbol.RangedDamage, 4, true),
      DiceSide(DiceSideSymbol.Disrupt, 2),
      DiceSide(DiceSideSymbol.Blank)
    )),
    5
  )

  val _28 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 28),
    "Teamwork",
    Affiliation.Hero,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 2, true),
      DiceSide(DiceSideSymbol.MeleeDamage, 2, true),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    2
  )

  val _29 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 29),
    "Poe Dameron's Blaster",
    Affiliation.Hero,
    CardColor.Red,
    true,
    CardRarity.Fixed,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 2),
      DiceSide(DiceSideSymbol.RangedDamage, 2, true),
      DiceSide(DiceSideSymbol.Disrupt, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    2
  )

  val _30 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 30),
    "Rey's Lightsaber",
    Affiliation.Hero,
    CardColor.Blue,
    true,
    CardRarity.Fixed,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.MeleeDamage, 2),
      DiceSide(DiceSideSymbol.MeleeDamage, 3, false, 1),
      DiceSide(DiceSideSymbol.MeleeDamage, 2, true),
      DiceSide(DiceSideSymbol.Shield, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Blank)
    )),
    3
  )

  val _31 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 31),
    "Inner Strength",
    Affiliation.Hero,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Support,
    Some(Dice(
      DiceSide(DiceSideSymbol.Focus, 1),
      DiceSide(DiceSideSymbol.Shield, 2),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    2
  )

  val _32 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 32),
    "Luke's Protection",
    Affiliation.Hero,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Support,
    None,
    0
  )

  val _33 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 33),
    "Horror Guard",
    Affiliation.Hero,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Support,
    None,
    1
  )

  val _34 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 34),
    "Direct Hit",
    Affiliation.Neutral,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    2
  )

  val _35 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 35),
    "ogistics",
    Affiliation.Neutral,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    0
  )

  val _36 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 36),
    "Recon",
    Affiliation.Neutral,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    1
  )

  val _37 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 37),
    "Strategic Planning",
    Affiliation.Hero,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    1
  )

  val _38 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 38),
    "Concentrate",
    Affiliation.Hero,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    1
  )

  val _39 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 39),
    "Defensive Stance",
    Affiliation.Hero,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    1
  )

  val _40 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 40),
    "Distraction",
    Affiliation.Hero,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    1
  )

  val _41 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 41),
    "Do Or Do Not",
    Affiliation.Hero,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    0
  )

  val _42 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 42),
    "Fierce Resolve",
    Affiliation.Hero,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    2
  )

  val _43 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 43),
    "Evade",
    Affiliation.Neutral,
    CardColor.Gray,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    1
  )

  val _44 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 44),
    "Flank",
    Affiliation.Neutral,
    CardColor.Gray,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    1
  )

  val _45 = DeckCard(
    CardId(CardSet.TwoPlayersGame, 45),
    "Sound The Alarm",
    Affiliation.Neutral,
    CardColor.Gray,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    0
  )

  val _46 = Battlefield(
    CardId(CardSet.TwoPlayersGame, 46),
    "Obi-Wan's Hut",
    Some("Tatooine"),
    CardRarity.Fixed
  )

}

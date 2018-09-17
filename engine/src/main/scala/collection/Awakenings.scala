package collection

import entities._

object Awakenings {

  val _1 = Character(
    CardId(CardSet.Awakenings, 1),
    "Captain Phasma",
    Some("Elite Trooper"),
    Affiliation.Villain,
    CardColor.Red,
    true,
    CardRarity.Legendary,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 1),
      DiceSide(DiceSideSymbol.RangedDamage, 2),
      DiceSide(DiceSideSymbol.Focus, 1),
      DiceSide(DiceSideSymbol.Discard, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Blank)
    )),
    CharacterPoints(12, Some(15)),
    11
  )

  val _2 = Character(
    CardId(CardSet.Awakenings, 2),
    "First Order Stormtrooper",
    None,
    Affiliation.Villain,
    CardColor.Red,
    false,
    CardRarity.Rare,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 1),
      DiceSide(DiceSideSymbol.RangedDamage, 2),
      DiceSide(DiceSideSymbol.RangedDamage, 2),
      DiceSide(DiceSideSymbol.Resource, 1, false, 1),
      DiceSide(DiceSideSymbol.Blank),
      DiceSide(DiceSideSymbol.Blank)
    )),
    CharacterPoints(7, None),
    7
  )

  val _3 = Character(
    CardId(CardSet.Awakenings, 3),
    "General Grievious",
    Some("Jedi Hunter"),
    Affiliation.Villain,
    CardColor.Red,
    true,
    CardRarity.Rare,
    Some(Dice(
      DiceSide(DiceSideSymbol.MeleeDamage, 2),
      DiceSide(DiceSideSymbol.MeleeDamage, 2),
      DiceSide(DiceSideSymbol.MeleeDamage, 3, false, 1),
      DiceSide(DiceSideSymbol.Disrupt, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Blank)
    )),
    CharacterPoints(13, Some(18)),
    10
  )

  val _6 = DeckCard(
    CardId(CardSet.Awakenings, 6),
    "First Order Tie Fighter",
    Affiliation.Villain,
    CardColor.Red,
    false,
    CardRarity.Rare,
    CardType.Support,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 1),
      DiceSide(DiceSideSymbol.RangedDamage, 2),
      DiceSide(DiceSideSymbol.RangedDamage, 3, false, 1),
      DiceSide(DiceSideSymbol.Disrupt, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    3
  )

  val _8 = DeckCard(
    CardId(CardSet.Awakenings, 8),
    "F-11D Rifle",
    Affiliation.Villain,
    CardColor.Red,
    false,
    CardRarity.Fixed,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 2, false, 1),
      DiceSide(DiceSideSymbol.RangedDamage, 1, true),
      DiceSide(DiceSideSymbol.RangedDamage, 2, true),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    2
  )

  val _11 = Character(
    CardId(CardSet.Awakenings, 11),
    "Kylo Ren",
    Some("Vader's Disciple"),
    Affiliation.Villain,
    CardColor.Blue,
    true,
    CardRarity.Fixed,
    Some(Dice(
      DiceSide(DiceSideSymbol.MeleeDamage, 1),
      DiceSide(DiceSideSymbol.MeleeDamage, 2, false, 1),
      DiceSide(DiceSideSymbol.Shield, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    CharacterPoints(10, Some(13)),
    11
  )

  val _14 = DeckCard(
    CardId(CardSet.Awakenings, 14),
    "Immobilize",
    Affiliation.Villain,
    CardColor.Blue,
    false,
    CardRarity.Rare,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.Disrupt, 1),
      DiceSide(DiceSideSymbol.Shield, 1),
      DiceSide(DiceSideSymbol.Shield, 2),
      DiceSide(DiceSideSymbol.Shield, 2),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Blank)
    )),
    2
  )

  val _17 = DeckCard(
    CardId(CardSet.Awakenings, 17),
    "Infantry Grenades",
    Affiliation.Villain,
    CardColor.Gray,
    false,
    CardRarity.Fixed,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 1),
      DiceSide(DiceSideSymbol.RangedDamage, 2, false, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank),
      DiceSide(DiceSideSymbol.Blank)
    )),
    2
  )

  val _38 = Character(
    CardId(CardSet.Awakenings, 38),
    "Rey",
    Some("Force Prodigy"),
    Affiliation.Hero,
    CardColor.Blue,
    true,
    CardRarity.Fixed,
    Some(Dice(
      DiceSide(DiceSideSymbol.MeleeDamage, 1),
      DiceSide(DiceSideSymbol.MeleeDamage, 2, true),
      DiceSide(DiceSideSymbol.Discard, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Resource, 1, true),
      DiceSide(DiceSideSymbol.Blank)
    )),
    CharacterPoints(9, Some(12)),
    10
  )

  val _40 = DeckCard(
    CardId(CardSet.Awakenings, 40),
    "Jedi Robes",
    Affiliation.Hero,
    CardColor.Blue,
    false,
    CardRarity.Rare,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.Focus, 1),
      DiceSide(DiceSideSymbol.Focus, 1),
      DiceSide(DiceSideSymbol.Disrupt, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Resource, 1, true),
      DiceSide(DiceSideSymbol.Blank)
    )),
    2
  )

  val _43 = DeckCard(
    CardId(CardSet.Awakenings, 43),
    "BB-8",
    Affiliation.Hero,
    CardColor.Gray,
    true,
    CardRarity.Rare,
    CardType.Support,
    Some(Dice(
      DiceSide(DiceSideSymbol.Focus, 1),
      DiceSide(DiceSideSymbol.Disrupt, 1),
      DiceSide(DiceSideSymbol.Shield, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    1
  )

  val _44 = DeckCard(
    CardId(CardSet.Awakenings, 44),
    "Rey's Staff",
    Affiliation.Hero,
    CardColor.Gray,
    true,
    CardRarity.Rare,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.MeleeDamage, 1),
      DiceSide(DiceSideSymbol.MeleeDamage, 1),
      DiceSide(DiceSideSymbol.MeleeDamage, 3, false, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    2
  )

  val _45 = Character(
    CardId(CardSet.Awakenings, 45),
    "Finn",
    Some("First Order Defector"),
    Affiliation.Hero,
    CardColor.Yellow,
    true,
    CardRarity.Fixed,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 1),
      DiceSide(DiceSideSymbol.RangedDamage, 2),
      DiceSide(DiceSideSymbol.MeleeDamage, 1),
      DiceSide(DiceSideSymbol.Shield, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Blank)
    )),
    CharacterPoints(13, Some(16)),
    10
  )

  val _57 = DeckCard(
    CardId(CardSet.Awakenings, 57),
    "Force Throw",
    Affiliation.Neutral,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 1),
      DiceSide(DiceSideSymbol.Disrupt, 1),
      DiceSide(DiceSideSymbol.Disrupt, 2),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    2
  )

  val _59 = DeckCard(
    CardId(CardSet.Awakenings, 59),
    "Lightsaber",
    Affiliation.Neutral,
    CardColor.Blue,
    false,
    CardRarity.Rare,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.MeleeDamage, 3, false, 1),
      DiceSide(DiceSideSymbol.MeleeDamage, 2, true),
      DiceSide(DiceSideSymbol.Shield, 1),
      DiceSide(DiceSideSymbol.Resource, 1),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    3
  )

  val _60 = DeckCard(
    CardId(CardSet.Awakenings, 60),
    "Mind Probe",
    Affiliation.Neutral,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Upgrade,
    Some(Dice(
      DiceSide(DiceSideSymbol.RangedDamage, 3, false, 1),
      DiceSide(DiceSideSymbol.Disrupt, 2),
      DiceSide(DiceSideSymbol.Discard, 2),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Special),
      DiceSide(DiceSideSymbol.Blank)
    )),
    2
  )

  val _75 = DeckCard(
    CardId(CardSet.Awakenings, 75),
    "The Best Defense...",
    Affiliation.Villain,
    CardColor.Red,
    false,
    CardRarity.Uncommon,
    CardType.Event,
    None,
    1
  )

  val _81 = DeckCard(
    CardId(CardSet.Awakenings, 81),
    "Enrage",
    Affiliation.Villain,
    CardColor.Blue,
    false,
    CardRarity.Common,
    CardType.Event,
    None,
    0
  )

  val _84 = DeckCard(
    CardId(CardSet.Awakenings, 84),
    "Intimidate",
    Affiliation.Villain,
    CardColor.Blue,
    false,
    CardRarity.Common,
    CardType.Event,
    None,
    0
  )

  val _89 = DeckCard(
    CardId(CardSet.Awakenings, 89),
    "Power of the Dark Side",
    Affiliation.Villain,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Support,
    None,
    2
  )

  val _91 = DeckCard(
    CardId(CardSet.Awakenings, 91),
    "Nowhere to Run",
    Affiliation.Villain,
    CardColor.Gray,
    false,
    CardRarity.Uncommon,
    CardType.Event,
    None,
    2
  )

  val _117 = DeckCard(
    CardId(CardSet.Awakenings, 117),
    "Heroism",
    Affiliation.Hero,
    CardColor.Blue,
    false,
    CardRarity.Common,
    CardType.Event,
    None,
    0
  )

  val _124 = DeckCard(
    CardId(CardSet.Awakenings, 124),
    "Awakening",
    Affiliation.Hero,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Support,
    None,
    1
  )

  val _126 = DeckCard(
    CardId(CardSet.Awakenings, 126),
    "Daring Escape",
    Affiliation.Hero,
    CardColor.Gray,
    false,
    CardRarity.Uncommon,
    CardType.Event,
    None,
    2
  )

  val _128 = DeckCard(
    CardId(CardSet.Awakenings, 128),
    "Draw Attention",
    Affiliation.Hero,
    CardColor.Yellow,
    false,
    CardRarity.Common,
    CardType.Event,
    None,
    0
  )

  val _130 = DeckCard(
    CardId(CardSet.Awakenings, 130),
    "Let the Wookie Win",
    Affiliation.Hero,
    CardColor.Yellow,
    false,
    CardRarity.Uncommon,
    CardType.Event,
    None,
    1
  )

  val _146 = DeckCard(
    CardId(CardSet.Awakenings, 146),
    "Disturbance in the Force",
    Affiliation.Neutral,
    CardColor.Blue,
    false,
    CardRarity.Common,
    CardType.Event,
    None,
    1
  )

  val _149 = DeckCard(
    CardId(CardSet.Awakenings, 149),
    "Use the Force",
    Affiliation.Neutral,
    CardColor.Blue,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    1
  )

  val _151 = DeckCard(
    CardId(CardSet.Awakenings, 151),
    "Aim",
    Affiliation.Neutral,
    CardColor.Gray,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    1
  )

  val _153 = DeckCard(
    CardId(CardSet.Awakenings, 153),
    "Block",
    Affiliation.Neutral,
    CardColor.Gray,
    false,
    CardRarity.Common,
    CardType.Event,
    None,
    2
  )

  val _154 = DeckCard(
    CardId(CardSet.Awakenings, 154),
    "Close Quarters Assault",
    Affiliation.Neutral,
    CardColor.Gray,
    false,
    CardRarity.Fixed,
    CardType.Event,
    None,
    0
  )

  val _155 = DeckCard(
    CardId(CardSet.Awakenings, 155),
    "Dodge",
    Affiliation.Neutral,
    CardColor.Gray,
    false,
    CardRarity.Common,
    CardType.Event,
    None,
    2
  )

  val _156 = DeckCard(
    CardId(CardSet.Awakenings, 156),
    "Flank",
    Affiliation.Neutral,
    CardColor.Gray,
    false,
    CardRarity.Uncommon,
    CardType.Event,
    None,
    1
  )

  val _157 = DeckCard(
    CardId(CardSet.Awakenings, 157),
    "Take Cover",
    Affiliation.Neutral,
    CardColor.Gray,
    false,
    CardRarity.Common,
    CardType.Event,
    None,
    0
  )

  val _162 = DeckCard(
    CardId(CardSet.Awakenings, 162),
    "Unpredictable",
    Affiliation.Neutral,
    CardColor.Yellow,
    false,
    CardRarity.Common,
    CardType.Event,
    None,
    0
  )

  val _168 = Battlefield(
    CardId(CardSet.Awakenings, 168),
    "Frozen Wastes",
    Some("Starkiller Base"),
    CardRarity.Fixed
  )

  val _174 = Battlefield(
    CardId(CardSet.Awakenings, 174),
    "Starship Graveyard",
    Some("Jakku"),
    CardRarity.Fixed
  )

  val allCards = Seq(
    _1,
    _2,
    _3,
//    _4,
//    _5,
    _6,
//    _7,
    _8,
//    _9,
//    _10,
    _11,
//    _12,
//    _13,
    _14,
//    _15,
//    _16,
    _17,
//    _18,
//    _19,
//    _20,
//    _21,
//    _22,
//    _23,
//    _24,
//    _25,
//    _26,
//    _27,
//    _28,
//    _29,
//    _30,
//    _31,
//    _32,
//    _33,
//    _34,
//    _35,
//    _36,
//    _37,
    _38,
//    _39,
    _40,
//    _41,
//    _42,
    _43,
    _44,
    _45,
//    _46,
//    _47,
//    _48,
//    _49,
//    _50,
//    _51,
//    _52,
//    _53,
//    _54,
//    _55,
//    _56,
    _57,
//    _58,
    _59,
    _60,
//    _61,
//    _62,
//    _63,
//    _64,
//    _65,
//    _66,
//    _67,
//    _68,
//    _69,
//    _70,
//    _71,
//    _72,
//    _73,
//    _74,
    _75,
//    _76,
//    _77,
//    _78,
//    _79,
//    _80,
    _81,
//    _82,
//    _83,
    _84,
//    _85,
//    _86,
//    _87,
//    _88,
    _89,
//    _90,
    _91,
//    _92,
//    _93,
//    _94,
//    _95,
//    _96,
//    _97,
//    _98,
//    _99,
//    _100,
//    _101,
//    _102,
//    _103,
//    _104,
//    _105,
//    _106,
//    _107,
//    _108,
//    _109,
//    _110,
//    _111,
//    _112,
//    _113,
//    _114,
//    _115,
//    _116,
    _117,
//    _118,
//    _119,
//    _120,
//    _121,
//    _122,
//    _123,
    _124,
//    _125,
    _126,
//    _127,
    _128,
//    _129,
    _130,
//    _131,
//    _132,
//    _133,
//    _134,
//    _135,
//    _136,
//    _137,
//    _138,
//    _139,
//    _140,
//    _141,
//    _142,
//    _143,
//    _144,
//    _145,
    _146,
//    _147,
//    _148,
    _149,
//    _150,
    _151,
//    _152,
    _153,
    _154,
    _155,
    _156,
    _157,
//    _158,
//    _159,
//    _160,
//    _161,
    _162,
//    _163,
//    _164,
//    _165,
//    _166,
//    _167,
    _168,
//    _169,
//    _170,
//    _171,
//    _172,
//    _173,
    _174
  )
}

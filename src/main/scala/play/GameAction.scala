package play

trait GameAction {
  def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean
  def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent]
}

case class MulliganAction(cards: Seq[Int]) extends GameAction {
  override def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
    phase == GamePhase.Mulligan && cards.forall(id => !playerArea.getCardInHand(id).isEmpty)
  }

  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent] = {
    var drawEvent: Option[DrawEvent] = None
    if (cards.nonEmpty) {
      val c = cards.map(id => playerArea.popCardFromHand(id)).foreach(c => playerArea.putCardInDeck(c.get))
      playerArea.shuffleDeck()
      val drawCards = playerArea.drawCards(cards.size)
      playerArea.putCardsInHand(drawCards)
      drawEvent = Some(DrawEvent(player, drawCards.map(_.uniqueId)))
    }

    Seq(MulliganEvent(player, cards)) ++ drawEvent
  }
}

case class PassAction() extends GameAction {
  override def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
    true
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent] = {
    Seq(PassEvent(player))
  }
}

case class ActivateAction(character: Int) extends GameAction {
  override def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
    true
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent] = {
    val character = playerArea.getCharacterOrSupport(card)
    Seq(PassEvent(player))
  }
}

case class ResolveDices(dices: Seq[Int]) extends GameAction {
  override def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
    true
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent] = {
    Seq(PassEvent(player))
  }
}

case class DiscardReroll(card: Int, dice: Int) extends GameAction {
  override def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
    true
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent] = {
    Seq(PassEvent(player))
  }
}

case class PlayCard(card: Int) extends GameAction {
  override def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
    true
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent] = {
    Seq(PassEvent(player))
  }
}

case class CardAbility(card: Int) extends GameAction {
  override def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
    true
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent] = {
    Seq(PassEvent(player))
  }
}

case class ClaimBattlefield() extends GameAction {
  override def isValid(phase: GamePhase.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Boolean = {
    true
  }
  override def process(player: Player.Value, playerArea: PlayerArea, opponentArea: PlayerArea): Seq[GameEvent] = {
    Seq(PassEvent(player))
  }
}
package com.lambtors.poker_api.module.poker.domain.model

import com.lambtors.poker_api.module.shared.syntax.Syntax._

final case class Card(cardSuit: CardSuit, cardValue: CardValue)

object Card {
  val allCards: List[Card] = (CardSuit.values cross CardValue.values).toList.map {
    case (cardSuit, cardValue) => Card(cardSuit, cardValue)
  }

  def randomCard: Card = Card(CardSuit.randomSuit, CardValue.randomValue)
}

object CardOrdering {
  val highestValueToLowest = new Ordering[Card] {
    override def compare(x: Card, y: Card): Int = {
      CardValueOrdering.highestValueToLowest.compare(x.cardValue, y.cardValue)
    }
  }
}

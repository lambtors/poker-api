package com.lambtors.poker_api.module.poker.domain.model

import com.lambtors.poker_api.module.shared.syntax.Syntax._

final case class Card(cardSuit: CardSuit, cardValue: CardValue)

object Card {
  val allCards: List[Card] = (CardSuit.all cross CardValue.all).toList.map {
    case (cardSuit, cardValue) => Card(cardSuit, cardValue)
  }

  def randomCard: Card = Card(CardSuit.randomSuit, CardValue.randomValue)
}

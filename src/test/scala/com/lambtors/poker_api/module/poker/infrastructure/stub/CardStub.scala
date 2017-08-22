package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.domain.model.{Card, CardSuit, CardValue}

object CardStub {
  def create(cardSuit: CardSuit = CardSuitStub.random(), cardValue: CardValue = CardValueStub.random()): Card =
    Card(cardSuit, cardValue)

  def random(): Card = create()
}

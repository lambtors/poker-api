package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.domain.model.{Card, CardSuit, CardValue}

import scala.util.Random

object CardStub {
  def create(cardSuit: CardSuit = CardSuitStub.random(), cardValue: CardValue = CardValueStub.random()): Card =
    Card(cardSuit, cardValue)

  def random(): Card = create()

  def randomDeck(): List[Card] = Random.shuffle(Card.allCards)
}

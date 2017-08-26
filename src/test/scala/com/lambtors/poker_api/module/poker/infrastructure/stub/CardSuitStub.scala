package com.lambtors.poker_api.module.poker.infrastructure.stub

import scala.util.Random

import com.lambtors.poker_api.module.poker.domain.model.CardSuit

object CardSuitStub {
  def create(cardSuit: String = Random.shuffle(CardSuit.values).head.suitName): CardSuit =
    CardSuit.values.find(_.suitName == cardSuit).get

  def random(): CardSuit = create()
}

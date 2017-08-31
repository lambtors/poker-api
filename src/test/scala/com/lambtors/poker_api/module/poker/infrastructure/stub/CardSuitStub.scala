package com.lambtors.poker_api.module.poker.infrastructure.stub

import scala.util.Random

import com.lambtors.poker_api.module.poker.domain.model.CardSuit

object CardSuitStub {
  def random(): CardSuit = Random.shuffle(CardSuit.all).head
}

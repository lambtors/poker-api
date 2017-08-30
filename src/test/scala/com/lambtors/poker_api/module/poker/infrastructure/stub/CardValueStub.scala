package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.domain.model.CardValue
import scala.util.Random

import com.lambtors.poker_api.module.poker.domain.model.CardValue.Ace

object CardValueStub {
  def random(): CardValue = Random.shuffle(CardValue.all).head

  def notAce(): CardValue = {
    val cardValue = random()
    if (cardValue != Ace) cardValue
    else notAce()
  }
}

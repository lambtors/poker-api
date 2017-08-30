package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.domain.model.{Ace, CardValue}
import scala.util.Random

object CardValueStub {
  def create(cardValue: String = Random.shuffle(CardValue.values).head.value): CardValue =
    CardValue.values.find(_.value == cardValue).get

  def notAce(): CardValue = {
    val cardValue = create()
    if (cardValue != Ace) cardValue
    else notAce()
  }

  def random(): CardValue = create()
}

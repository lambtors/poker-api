package com.lambtors.poker_api.module.poker.infrastructure.stub

import scala.util.Random

import com.lambtors.poker_api.module.poker.domain.model.CardValue

object CardValueStub {
  def random(): CardValue = Random.shuffle(CardValue.all).head
}

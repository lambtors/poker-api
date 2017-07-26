package com.lambtors.poker_api.module.poker.infrastructure.stub

import scala.util.Random

object NumberStub {
  def randomBetweenInclusive(lower: Int, upper: Int): Int = lower + Random.nextInt((upper - lower) + 1)
}

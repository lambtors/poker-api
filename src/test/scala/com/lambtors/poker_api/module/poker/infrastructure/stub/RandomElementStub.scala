package com.lambtors.poker_api.module.poker.infrastructure.stub

import scala.util.Random

object RandomElementStub {
  def oneOf[A](elements: A*): A = Random.shuffle(elements.toList).head
}

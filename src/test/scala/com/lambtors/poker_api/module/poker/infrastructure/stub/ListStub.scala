package com.lambtors.poker_api.module.poker.infrastructure.stub

import scala.util.Random

object ListStub {
  def randomElements[A](creator: () => A): List[A] =
    randomElements(Random.nextInt(100), creator)

  def randomElementsCappedAt[A](maximumElements: Int, creator: () => A): List[A] =
    randomElements(Random.nextInt(maximumElements), creator)

  def randomElements[A](amount: Int, creator: () => A): List[A] =
    (0 to amount).toList.map(_ => creator())
}

package com.lambtors.poker_api.module.poker.behaviour

import com.lambtors.poker_api.module.poker.domain.PokerGameRepository
import org.scalamock.scalatest.MockFactory
import org.scalatest.{OneInstancePerTest, WordSpec}
import org.scalatest.concurrent.ScalaFutures

trait PokerBehaviourSpec
    extends WordSpec
    with MockFactory
    with OneInstancePerTest
    with ScalaFutures
    with MockedPokerGameRepository

trait MockedPokerGameRepository extends MockFactory {
  protected val pokerGameRepository: PokerGameRepository = mock[PokerGameRepository]
}

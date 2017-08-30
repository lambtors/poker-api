package com.lambtors.poker_api.module.poker.unit

import com.lambtors.poker_api.module.poker.domain.model._
import com.lambtors.poker_api.module.poker.infrastructure.stub.CardStub
import org.scalatest.{Matchers, WordSpec}

final class CardOrderingTest extends WordSpec with Matchers {
  "A highestValueToLowest CardOrdering" should {
    "order cards from highest value to lowest" in {

      val two   = CardStub.create(cardValue = Two)
      val three = CardStub.create(cardValue = Three)
      val kay   = CardStub.create(cardValue = Kay)
      val ace   = CardStub.create(cardValue = Ace)

      val cards         = List(two, three, kay, ace)
      val expectedOrder = List(ace, kay, three, two)

      cards.sorted(CardOrdering.highestValueToLowest) should contain theSameElementsInOrderAs expectedOrder
    }
  }
}

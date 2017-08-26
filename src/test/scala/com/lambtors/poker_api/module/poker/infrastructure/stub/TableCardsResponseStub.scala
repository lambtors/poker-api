package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.domain.model.{Card, TableCardsResponse}

object TableCardsResponseStub {
  def create(cards: List[Card] = PokerGameStub.create().tableCards): TableCardsResponse = TableCardsResponse(cards)

  def random(): TableCardsResponse = create()
}

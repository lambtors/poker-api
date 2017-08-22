package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.application.player_cards.find.FindPlayerCardsResponse
import com.lambtors.poker_api.module.poker.domain.model.Card

object FindPlayerCardsResponseStub {
  def create(cards: List[Card] = ListStub.randomElements(() => CardStub.random())): FindPlayerCardsResponse =
    FindPlayerCardsResponse(cards)

  def random(): FindPlayerCardsResponse = create()
}

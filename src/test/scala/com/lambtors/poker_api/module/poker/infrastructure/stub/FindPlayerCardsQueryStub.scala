package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.application.player_cards.find.FindPlayerCardsQuery

object FindPlayerCardsQueryStub {
  def create(playerId: String = FakePlayerIdStub.random().playerId.toString): FindPlayerCardsQuery =
    FindPlayerCardsQuery(playerId)

  def random(): FindPlayerCardsQuery = create()
}

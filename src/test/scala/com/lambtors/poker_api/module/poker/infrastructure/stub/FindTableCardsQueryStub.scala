package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.application.table.find.FindTableCardsQuery

object FindTableCardsQueryStub {
  def create(gameId: String = GameIdStub.random().gameId.toString): FindTableCardsQuery = FindTableCardsQuery(gameId)

  def random(): FindTableCardsQuery = create()
}

package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.application.win.FindGameWinnersQuery

object FindGameWinnersQueryStub {
  def create(gameId: String = GameIdStub.random().gameId.toString): FindGameWinnersQuery =
    FindGameWinnersQuery(gameId)

  def random(): FindGameWinnersQuery = create()

  def invalid(): FindGameWinnersQuery = create(GameIdStub.invalid())
}

package com.lambtors.poker_api.module.poker.infrastructure.stub

import java.util.UUID

import com.lambtors.poker_api.module.poker.domain.model.GameId

object GameIdStub {
  def create(gameId: UUID): GameId = GameId(gameId)

  def random(): GameId = create(UUID.randomUUID())
}

package com.lambtors.poker_api.module.poker.infrastructure.stub

import java.util.UUID

import com.lambtors.poker_api.module.poker.domain.model.FakePlayerId

object FakePlayerIdStub {
  def create(playerId: UUID = UUID.randomUUID()): FakePlayerId =
    FakePlayerId(playerId)

  def random(): FakePlayerId = create()
}

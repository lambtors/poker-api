package com.lambtors.poker_api.module.poker.infrastructure.stub

import java.util.UUID

import com.lambtors.poker_api.module.poker.domain.model.PlayerId

object PlayerIdStub {
  def create(playerId: UUID = UUID.randomUUID()): PlayerId = PlayerId(playerId)

  def random(): PlayerId = create()

  def invalid(): String = ""
}

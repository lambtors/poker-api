package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.application.river.AddRiverCardToTableCommand

object AddRiverCardToTableCommandStub {
  def create(gameId: String = GameIdStub.random().gameId.toString): AddRiverCardToTableCommand =
    AddRiverCardToTableCommand(gameId)

  def random(): AddRiverCardToTableCommand = create()
}

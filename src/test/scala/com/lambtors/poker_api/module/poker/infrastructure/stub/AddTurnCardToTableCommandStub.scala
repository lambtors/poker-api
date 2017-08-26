package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.application.turn.AddTurnCardToTableCommand

object AddTurnCardToTableCommandStub {
  def create(gameId: String = GameIdStub.random().gameId.toString): AddTurnCardToTableCommand =
    AddTurnCardToTableCommand(gameId)

  def random(): AddTurnCardToTableCommand = create()
}

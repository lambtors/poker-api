package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.application.flop.AddFlopCardsToTableCommand

object AddFlopCardsToTableCommandStub {
  def create(gameId: String = GameIdStub.random().gameId.toString): AddFlopCardsToTableCommand =
    AddFlopCardsToTableCommand(gameId)

  def random(): AddFlopCardsToTableCommand = create()
}

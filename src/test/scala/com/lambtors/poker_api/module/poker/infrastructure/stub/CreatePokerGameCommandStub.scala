package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.application.create.CreatePokerGameCommand

object CreatePokerGameCommandStub {
  def create(amountOfPlayers: Int = AmountOfPlayersStub.random().amount,
             gameId: String = GameIdStub.random().gameId.toString): CreatePokerGameCommand =
    CreatePokerGameCommand(amountOfPlayers, gameId)

  def random(): CreatePokerGameCommand = create()
}

package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.application.create.CreatePokerGameCommand

object CreatePokerGameCommandStub {
  def create(amountOfPlayers: Int, gameId: String): CreatePokerGameCommand =
    CreatePokerGameCommand(amountOfPlayers, gameId)

  def random(): CreatePokerGameCommand =
    create(AmountOfPlayersStub.random().amount, GameIdStub.random().gameId.toString)
}

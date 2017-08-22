package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.domain.model.{AmountOfPlayers, GameId, PokerGame}

object PokerGameStub {
  def create(gameId: GameId = GameIdStub.random(),
             amountOfPlayers: AmountOfPlayers = AmountOfPlayersStub.random()): PokerGame =
    PokerGame(gameId, amountOfPlayers, List.empty)

  def random: PokerGame = create()
}

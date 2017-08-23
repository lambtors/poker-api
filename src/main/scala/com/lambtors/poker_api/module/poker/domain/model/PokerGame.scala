package com.lambtors.poker_api.module.poker.domain.model

final case class PokerGame(gameId: GameId, amountOfPlayers: AmountOfPlayers, tableCards: List[Card])

package com.lambtors.poker_api.module.poker.domain

import com.lambtors.poker_api.module.poker.domain.model.{GameId, PokerGame}

trait PokerGameRepository[P[_]] {
  def search(gameId: GameId): P[Option[PokerGame]]

  def update(pokerGame: PokerGame): P[Unit]

  def insert(pokerGame: PokerGame): P[Unit]
}

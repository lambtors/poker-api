package com.lambtors.poker_api.module.poker.domain

import cats.data.OptionT
import com.lambtors.poker_api.module.poker.domain.model.{GameId, PokerGame}

trait PokerGameRepository[P[_]] {
  def search(gameId: GameId): OptionT[P, PokerGame]

  def update(pokerGame: PokerGame): P[Unit]

  def insert(pokerGame: PokerGame): P[Unit]
}

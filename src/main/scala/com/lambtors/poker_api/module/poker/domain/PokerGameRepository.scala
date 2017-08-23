package com.lambtors.poker_api.module.poker.domain

import scala.concurrent.Future

import com.lambtors.poker_api.module.poker.domain.model.{GameId, PokerGame}

trait PokerGameRepository {
  def search(gameId: GameId): Future[Option[PokerGame]]

  def update(pokerGame: PokerGame): Future[Unit]

  def insert(pokerGame: PokerGame): Future[Unit]
}

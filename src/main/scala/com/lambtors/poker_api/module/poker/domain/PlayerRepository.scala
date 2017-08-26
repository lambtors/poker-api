package com.lambtors.poker_api.module.poker.domain

import scala.concurrent.Future

import com.lambtors.poker_api.module.poker.domain.model.{GameId, Player, PlayerId}

trait PlayerRepository {
  def search(playerId: PlayerId): Future[Option[Player]]
  def search(gameId: GameId): Future[List[Player]]
  def insert(player: Player): Future[Unit]
}

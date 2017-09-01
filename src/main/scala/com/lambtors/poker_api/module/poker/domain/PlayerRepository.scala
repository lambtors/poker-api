package com.lambtors.poker_api.module.poker.domain

import com.lambtors.poker_api.module.poker.domain.model.{GameId, Player, PlayerId}

trait PlayerRepository[P[_]] {
  def search(playerId: PlayerId): P[Option[Player]]
  def search(gameId: GameId): P[List[Player]]
  def insert(player: Player): P[Unit]
}

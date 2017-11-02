package com.lambtors.poker_api.module.poker.domain

import cats.data.OptionT
import com.lambtors.poker_api.module.poker.domain.model.{GameId, Player, PlayerId}

trait PlayerRepository[P[_]] {
  def search(playerId: PlayerId): OptionT[P, Player]
  def search(gameId: GameId): P[List[Player]]
  def insert(player: Player): P[Unit]
}

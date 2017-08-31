package com.lambtors.poker_api.module.poker.application.player_cards.find

import scala.concurrent.{ExecutionContext, Future}

import com.lambtors.poker_api.module.poker.domain.PlayerRepository
import com.lambtors.poker_api.module.poker.domain.error.PlayerNotFound
import com.lambtors.poker_api.module.poker.domain.model.{Card, PlayerId}

final class PlayerCardsFinder(playerRepository: PlayerRepository[Future])(implicit ec: ExecutionContext) {
  def find(playerId: PlayerId): Future[(Card, Card)] =
    playerRepository
      .search(playerId)
      .flatMap(
        _.fold[Future[(Card, Card)]](Future.failed(PlayerNotFound(playerId)))(player =>
          Future.successful((player.firstCard, player.secondCard)))
      )
}

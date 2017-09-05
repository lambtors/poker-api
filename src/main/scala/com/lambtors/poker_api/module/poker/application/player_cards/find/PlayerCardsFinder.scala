package com.lambtors.poker_api.module.poker.application.player_cards.find

import cats.implicits._
import com.lambtors.poker_api.module.poker.domain.PlayerRepository
import com.lambtors.poker_api.module.poker.domain.error.PlayerNotFound
import com.lambtors.poker_api.module.poker.domain.model.{Card, PlayerId}
import com.lambtors.poker_api.module.shared.domain.types.ThrowableTypeClasses.MonadErrorThrowable

final class PlayerCardsFinder[P[_]: MonadErrorThrowable](playerRepository: PlayerRepository[P]) {
  def find(playerId: PlayerId): P[(Card, Card)] =
    playerRepository
      .search(playerId)
      .fold[P[(Card, Card)]](MonadErrorThrowable[P].raiseError(PlayerNotFound(playerId)))(player =>
        player.cards.pure[P])
      .flatten
}

package com.lambtors.poker_api.module.poker.application.player_cards.find

import cats.Functor
import cats.implicits._
import com.lambtors.poker_api.infrastructure.query_bus.QueryHandler
import com.lambtors.poker_api.module.poker.domain.model.PlayerId
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final class FindPlayerCardsQueryHandler[P[_]: Functor](playerCardsFinder: PlayerCardsFinder[P])
    extends QueryHandler[P, FindPlayerCardsQuery, FindPlayerCardsResponse] {

  override def handle(query: FindPlayerCardsQuery): Validation[P[FindPlayerCardsResponse]] =
    validate(query).map(playerId => playerCardsFinder.find(playerId).map(FindPlayerCardsResponse))

  private def validate(query: FindPlayerCardsQuery) = PlayerId.fromString(query.playerId)
}

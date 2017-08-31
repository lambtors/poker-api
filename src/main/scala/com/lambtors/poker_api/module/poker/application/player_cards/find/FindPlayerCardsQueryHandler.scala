package com.lambtors.poker_api.module.poker.application.player_cards.find

import scala.concurrent.{ExecutionContext, Future}

import com.lambtors.poker_api.infrastructure.query_bus.QueryHandler
import com.lambtors.poker_api.module.poker.domain.model.PlayerId
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final class FindPlayerCardsQueryHandler(playerCardsFinder: PlayerCardsFinder)(implicit ec: ExecutionContext)
    extends QueryHandler[FindPlayerCardsQuery, FindPlayerCardsResponse] {

  override def handle(query: FindPlayerCardsQuery): Validation[Future[FindPlayerCardsResponse]] =
    validate(query).map(playerId => playerCardsFinder.find(playerId).map(FindPlayerCardsResponse))

  private def validate(query: FindPlayerCardsQuery) = PlayerId.fromString(query.playerId)
}

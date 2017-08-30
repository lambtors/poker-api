package com.lambtors.poker_api.module.poker.application.player_cards.find

import scala.concurrent.{ExecutionContext, Future}

import com.lambtors.poker_api.infrastructure.query_bus.QueryHandler
import com.lambtors.poker_api.module.poker.domain.model.PlayerId

final class FindPlayerCardsQueryHandler(playerCardsFinder: PlayerCardsFinder)(implicit ec: ExecutionContext)
    extends QueryHandler[FindPlayerCardsQuery, FindPlayerCardsResponse] {

  override def handle(query: FindPlayerCardsQuery): Future[FindPlayerCardsResponse] =
    validate(query).flatMap(playerCardsFinder.find).map(FindPlayerCardsResponse)

  def validate(query: FindPlayerCardsQuery): Future[PlayerId] = PlayerId.fromString(query.playerId)
}

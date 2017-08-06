package com.lambtors.poker_api.module.poker.application.table.find

import com.lambtors.poker_api.infrastructure.query_bus.QueryHandler
import com.lambtors.poker_api.module.poker.domain.model.{Card, GameId}

import scala.concurrent.{ExecutionContext, Future}

final class FindTableCardsQueryHandler(tableCardsFinder: TableCardsFinder)
                                      (implicit ec: ExecutionContext)
  extends QueryHandler[FindTableCardsQuery, List[Card]] {
  override def handle(query: FindTableCardsQuery): Future[List[Card]] =
    validate(query).flatMap(tableCardsFinder.find)

  private def validate(query: FindTableCardsQuery) =
    GameId.fromString(query.gameId)
}

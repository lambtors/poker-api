package com.lambtors.poker_api.module.poker.application.table.find

import com.lambtors.poker_api.infrastructure.query_bus.QueryHandler
import com.lambtors.poker_api.module.poker.domain.model.{GameId, TableCardsResponse}
import scala.concurrent.{ExecutionContext, Future}

import com.lambtors.poker_api.infrastructure.query_bus.QueryHandler
import com.lambtors.poker_api.module.poker.domain.model.{GameId, TableCardsResponse}

final class FindTableCardsQueryHandler(tableCardsFinder: TableCardsFinder)(implicit ec: ExecutionContext)
    extends QueryHandler[FindTableCardsQuery, TableCardsResponse] {
  override def handle(query: FindTableCardsQuery): Future[TableCardsResponse] =
    validate(query).flatMap(tableCardsFinder.find)

  private def validate(query: FindTableCardsQuery) =
    GameId.fromString(query.gameId)
}

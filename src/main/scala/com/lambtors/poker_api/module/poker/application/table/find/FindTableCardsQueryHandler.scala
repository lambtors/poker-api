package com.lambtors.poker_api.module.poker.application.table.find

import com.lambtors.poker_api.infrastructure.query_bus.QueryHandler
import com.lambtors.poker_api.module.poker.domain.model.{GameId, TableCardsResponse}
import scala.concurrent.Future

import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final class FindTableCardsQueryHandler(tableCardsFinder: TableCardsFinder)
  extends QueryHandler[FindTableCardsQuery, TableCardsResponse] {
  override def handle(query: FindTableCardsQuery): Validation[Future[TableCardsResponse]] =
    GameId.fromString(query.gameId).map(tableCardsFinder.find)
}

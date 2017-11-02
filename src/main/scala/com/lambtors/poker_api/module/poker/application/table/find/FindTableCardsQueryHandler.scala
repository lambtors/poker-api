package com.lambtors.poker_api.module.poker.application.table.find

import cats.Functor
import com.lambtors.poker_api.infrastructure.query_bus.QueryHandler
import com.lambtors.poker_api.module.poker.domain.model.{GameId, TableCardsResponse}
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final class FindTableCardsQueryHandler[P[_]: Functor](tableCardsFinder: TableCardsFinder[P])
    extends QueryHandler[P, FindTableCardsQuery, TableCardsResponse] {
  override def handle(query: FindTableCardsQuery): Validation[P[TableCardsResponse]] =
    GameId.fromString(query.gameId).map(tableCardsFinder.find)
}

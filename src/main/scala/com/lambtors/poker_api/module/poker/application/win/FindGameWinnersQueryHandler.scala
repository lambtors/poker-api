package com.lambtors.poker_api.module.poker.application.win

import cats.Functor
import cats.implicits._

import com.lambtors.poker_api.infrastructure.query_bus.QueryHandler
import com.lambtors.poker_api.module.poker.domain.model.GameId
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final class FindGameWinnersQueryHandler[P[_]: Functor](gameWinnerFinder: GameWinnersFinder[P])
    extends QueryHandler[P, FindGameWinnersQuery, FindGameWinnersResponse] {
  override def handle(query: FindGameWinnersQuery): Validation[P[FindGameWinnersResponse]] =
    GameId
      .fromString(query.gameId)
      .map(
        gameWinnerFinder
          .findWinners(_)
          .map(winners => FindGameWinnersResponse(winners.map(_.playerId.playerId.toString))))
}

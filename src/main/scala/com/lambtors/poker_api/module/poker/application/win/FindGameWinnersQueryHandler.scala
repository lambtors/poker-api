package com.lambtors.poker_api.module.poker.application.win

import scala.concurrent.{ExecutionContext, Future}

import com.lambtors.poker_api.infrastructure.query_bus.QueryHandler
import com.lambtors.poker_api.module.poker.domain.model.GameId
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final class FindGameWinnersQueryHandler(gameWinnerFinder: GameWinnersFinder)(implicit ec: ExecutionContext)
    extends QueryHandler[FindGameWinnersQuery, FindGameWinnersResponse] {
  override def handle(query: FindGameWinnersQuery): Validation[Future[FindGameWinnersResponse]] =
    GameId
      .fromString(query.gameId)
      .map(
        gameId =>
          gameWinnerFinder
            .findWinners(gameId)
            .map(winners => FindGameWinnersResponse(winners.map(_.playerId.playerId.toString))))
}

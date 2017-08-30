package com.lambtors.poker_api.module.poker.application.win

import scala.concurrent.{ExecutionContext, Future}

import com.lambtors.poker_api.infrastructure.query_bus.QueryHandler
import com.lambtors.poker_api.module.poker.domain.model.GameId

final class FindGameWinnersQueryHandler(gameWinnerFinder: GameWinnersFinder)(implicit ec: ExecutionContext)
    extends QueryHandler[FindGameWinnersQuery, FindGameWinnersResponse] {
  override def handle(query: FindGameWinnersQuery): Future[FindGameWinnersResponse] =
    GameId
      .fromString(query.gameId)
      .flatMap(gameWinnerFinder.findWinners)
      .map(winners => FindGameWinnersResponse(winners.map(_.playerId.playerId.toString)))
}

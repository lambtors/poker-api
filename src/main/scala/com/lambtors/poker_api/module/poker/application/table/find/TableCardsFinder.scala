package com.lambtors.poker_api.module.poker.application.table.find

import com.lambtors.poker_api.module.poker.domain.PokerGameRepository
import com.lambtors.poker_api.module.poker.domain.error.PokerGameNotFound
import com.lambtors.poker_api.module.poker.domain.model.{GameId, TableCardsResponse}
import scala.concurrent.{ExecutionContext, Future}

final class TableCardsFinder(repository: PokerGameRepository)(implicit ec: ExecutionContext) {
  def find(gameId: GameId): Future[TableCardsResponse] =
    repository
      .search(gameId)
      .flatMap(_.fold(Future.failed[TableCardsResponse](PokerGameNotFound(gameId)))(game =>
        Future.successful(TableCardsResponse(game.tableCards))))
}

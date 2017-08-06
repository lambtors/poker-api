package com.lambtors.poker_api.module.poker.application.table.find

import com.lambtors.poker_api.module.poker.domain.PokerGameRepository
import com.lambtors.poker_api.module.poker.domain.error.PokerGameNotFound
import com.lambtors.poker_api.module.poker.domain.model.{Card, GameId}

import scala.concurrent.{ExecutionContext, Future}

final class TableCardsFinder(repository: PokerGameRepository)
                            (implicit ec: ExecutionContext) {
  def find(gameId: GameId): Future[List[Card]] = repository.search(gameId).map(
    gameOpt => if (gameOpt.isDefined) {
      gameOpt.get.tableCards
    } else {
      throw PokerGameNotFound(gameId)
    }
  )
}

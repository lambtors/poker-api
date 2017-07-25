package com.lambtors.poker_api.module.poker.application.create

import scala.concurrent.{ExecutionContext, Future}

import com.lambtors.poker_api.module.poker.domain.PokerGameRepository
import com.lambtors.poker_api.module.poker.domain.error.PokerGameAlreadyExisting
import com.lambtors.poker_api.module.poker.domain.model.{AmountOfPlayers, GameId, PokerGame}

final class PokerGameCreator(pokerGameRepository: PokerGameRepository)(implicit ec: ExecutionContext) {
  def create(amountOfPlayers: AmountOfPlayers, gameId: GameId): Future[Unit] = {
    pokerGameRepository
      .search(gameId)
      .flatMap(_.fold(pokerGameRepository.insert(PokerGame(gameId, amountOfPlayers)))(_ =>
        Future.failed[Unit](PokerGameAlreadyExisting(gameId))))
  }
}

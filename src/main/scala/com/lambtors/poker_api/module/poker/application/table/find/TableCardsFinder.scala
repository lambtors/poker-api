package com.lambtors.poker_api.module.poker.application.table.find

import cats.implicits._
import com.lambtors.poker_api.module.poker.domain.PokerGameRepository
import com.lambtors.poker_api.module.poker.domain.error.PokerGameNotFound
import com.lambtors.poker_api.module.poker.domain.model.{GameId, PokerGame, TableCardsResponse}
import com.lambtors.poker_api.module.shared.domain.types.ThrowableTypeClasses.MonadErrorThrowable

final class TableCardsFinder[P[_]: MonadErrorThrowable](repository: PokerGameRepository[P]) {
  def find(gameId: GameId): P[TableCardsResponse] =
    repository
      .search(gameId)
      .flatMap(
        _.fold[P[TableCardsResponse]](MonadErrorThrowable[P].raiseError(PokerGameNotFound(gameId)))(
          (game: PokerGame) => TableCardsResponse(game.tableCards).pure[P]))
}

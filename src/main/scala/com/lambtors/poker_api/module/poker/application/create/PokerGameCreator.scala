package com.lambtors.poker_api.module.poker.application.create

import scala.concurrent.{ExecutionContext, Future}

import cats.implicits._
import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.error.PokerGameAlreadyExisting
import com.lambtors.poker_api.module.poker.domain.model._
import com.lambtors.poker_api.module.shared.domain.{DeckProvider, UUIDProvider}
import com.lambtors.poker_api.module.shared.domain.types.ThrowableTypeClasses.MonadErrorThrowable

final class PokerGameCreator[P[_]: MonadErrorThrowable](
    pokerGameRepository: PokerGameRepository[P],
    playerRepository: PlayerRepository[P],
    uUIDProvider: UUIDProvider,
    deckProvider: DeckProvider
)(implicit ec: ExecutionContext) {
  def create(amountOfPlayers: AmountOfPlayers, gameId: GameId): P[Unit] =
    pokerGameRepository
      .search(gameId)
      .flatMap(_.fold {
        pokerGameRepository
          .insert(PokerGame(gameId, amountOfPlayers, List.empty))
          .map(_ => {
            var cards = deckProvider.provide()

            (1 to amountOfPlayers.amount).foreach(_ => {
              val firstCard :: cardsWithoutFirstCard           = cards
              val secondCard :: cardsWithoutFirstAndSecondCard = cardsWithoutFirstCard
              cards = cardsWithoutFirstAndSecondCard
              playerRepository.insert(Player(PlayerId(uUIDProvider.provide()), gameId, firstCard, secondCard))
            })
          })
      }(_ => MonadErrorThrowable[P].raiseError(PokerGameAlreadyExisting(gameId))))
}

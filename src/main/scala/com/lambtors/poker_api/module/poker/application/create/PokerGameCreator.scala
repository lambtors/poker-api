package com.lambtors.poker_api.module.poker.application.create

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
) {
  def create(amountOfPlayers: AmountOfPlayers, gameId: GameId): P[Unit] = {
    pokerGameRepository
      .search(gameId)
      .fold {
        pokerGameRepository
          .insert(PokerGame(gameId, amountOfPlayers, List.empty))
          .flatMap(
            _ => {
              var cards = deckProvider.provide()

              (1 to amountOfPlayers.amount).toList
                .traverse_(
                  _ => {
                    val firstCard :: cardsWithoutFirstCard           = cards
                    val secondCard :: cardsWithoutFirstAndSecondCard = cardsWithoutFirstCard
                    cards = cardsWithoutFirstAndSecondCard
                    playerRepository.insert(Player(PlayerId(uUIDProvider.provide()), gameId, firstCard, secondCard))
                  }
                )
            }
          )
      }(_ => MonadErrorThrowable[P].raiseError(PokerGameAlreadyExisting(gameId)))
      .flatten
  }
}

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
    uUIDProvider: UUIDProvider[P],
    deckProvider: DeckProvider[P]
) {
  def create(amountOfPlayers: AmountOfPlayers, gameId: GameId): P[Unit] = {
    pokerGameRepository
      .search(gameId)
      .fold {
        pokerGameRepository
          .insert(PokerGame(gameId, amountOfPlayers, List.empty))
          .flatMap(
            _ => {
              deckProvider.provide().flatMap { cards =>
                var leftoverCards = cards
                (1 to amountOfPlayers.amount).toList
                  .traverse_(
                    _ => {
                      val firstCard :: secondCard :: otherCards = leftoverCards
                      leftoverCards = otherCards
                      uUIDProvider
                        .provide()
                        .flatMap(uuid =>
                          playerRepository.insert(Player(PlayerId(uuid), gameId, firstCard, secondCard)))
                    }
                  )
              }
            }
          )
      }(_ => MonadErrorThrowable[P].raiseError(PokerGameAlreadyExisting(gameId)))
      .flatten
  }
}

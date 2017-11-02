package com.lambtors.poker_api.module.poker.application.flop

import cats.implicits._

import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.error.{FlopNotPossibleWhenItIsAlreadyGiven, PokerGameNotFound}
import com.lambtors.poker_api.module.poker.domain.model.{Card, GameId, Player}
import com.lambtors.poker_api.module.shared.domain.DeckProvider
import com.lambtors.poker_api.module.shared.domain.types.ThrowableTypeClasses.MonadErrorThrowable

final class FlopCardsAdder[P[_]: MonadErrorThrowable](
    repository: PokerGameRepository[P],
    playerRepository: PlayerRepository[P],
    deckProvider: DeckProvider
) {
  def add(gameId: GameId): P[Unit] =
    repository
      .search(gameId)
      .flatMap(
        _.fold[P[Unit]](MonadErrorThrowable[P].raiseError(PokerGameNotFound(gameId)))(game =>
          thereAreCardAtTheTable(game.tableCards).ifM(
            MonadErrorThrowable[P].raiseError(FlopNotPossibleWhenItIsAlreadyGiven(gameId)),
            playerRepository
              .search(gameId)
              .flatMap(
                players =>
                  repository.update(
                    game.copy(tableCards = deckProvider.shuffleGivenDeck(availableCards(players)).take(3))
                )
              )
        )))

  private def availableCards(players: List[Player]) =
    deckProvider.provide().filterNot(card => playerCards(players).contains(card))

  private def playerCards(players: List[Player]) = players.flatMap(player => List(player.firstCard, player.secondCard))

  private def thereAreCardAtTheTable(tableCards: List[Card]): P[Boolean] = tableCards.nonEmpty.pure[P]
}

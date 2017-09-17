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
    deckProvider: DeckProvider[P]
) {
  def add(gameId: GameId): P[Unit] = {
    repository
      .search(gameId)
      .fold[P[Unit]](MonadErrorThrowable[P].raiseError(PokerGameNotFound(gameId)))(game =>
        thereAreCardsAtTable(game.tableCards).ifM(
          MonadErrorThrowable[P].raiseError(FlopNotPossibleWhenItIsAlreadyGiven(gameId)),
          playerRepository
            .search(gameId)
            .flatMap(
              players =>
                shuffleAvailableCards(players)
                  .flatMap(shuffledCards => repository.update(game.copy(tableCards = shuffledCards.take(3))))
            )
      ))
      .flatten
  }

  private def shuffleAvailableCards(players: List[Player]): P[List[Card]] =
    deckProvider
      .provide()
      .flatMap(cards => deckProvider.shuffleGivenDeck(cards.filterNot(card => playerCards(players).contains(card))))

  private def playerCards(players: List[Player]) = players.flatMap(player => List(player.firstCard, player.secondCard))

  private def thereAreCardsAtTable(tableCards: List[Card]): P[Boolean] = tableCards.nonEmpty.pure[P]
}

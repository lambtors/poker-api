package com.lambtors.poker_api.module.poker.application.turn

import cats.implicits._
import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.error.{
  PokerGameNotFound,
  TurnNotPossibleWhenFlopIsNotGiven,
  TurnNotPossibleWhenItIsAlreadyGiven
}
import com.lambtors.poker_api.module.poker.domain.model.{Card, GameId, Player}
import com.lambtors.poker_api.module.shared.domain.DeckProvider
import com.lambtors.poker_api.module.shared.domain.types.ThrowableTypeClasses.MonadErrorThrowable

final class TurnCardAdder[P[_]: MonadErrorThrowable](
    repository: PokerGameRepository[P],
    playerRepository: PlayerRepository[P],
    deckProvider: DeckProvider
) {
  def add(gameId: GameId): P[Unit] =
    repository
      .search(gameId)
      .flatMap(
        _.fold[P[Unit]](MonadErrorThrowable[P].raiseError(PokerGameNotFound(gameId)))(game =>
          cardsAtTableNumberIsGreaterThanThree(game.tableCards).ifM(
            MonadErrorThrowable[P].raiseError(TurnNotPossibleWhenItIsAlreadyGiven(gameId)),
            cardsAtTableNumberIsLowerThanThree(game.tableCards).ifM(
              MonadErrorThrowable[P].raiseError(TurnNotPossibleWhenFlopIsNotGiven(gameId)),
              playerRepository
                .search(gameId)
                .flatMap(
                  players =>
                    repository.update(
                      game.copy(
                        tableCards = game.tableCards ++ deckProvider
                          .shuffleGivenDeck(availableCards(playersCards(players) ++ game.tableCards))
                          .take(1)
                      )
                  )
                )
            )
        )))

  private def availableCards(cardsInGame: List[Card]) =
    deckProvider.provide().filterNot(card => cardsInGame.contains(card))

  private def playersCards(players: List[Player]) =
    players.flatMap(player => List(player.firstCard, player.secondCard))

  private def cardsAtTableNumberIsGreaterThanThree(tableCards: List[Card]): P[Boolean] = (tableCards.length > 3).pure[P]

  private def cardsAtTableNumberIsLowerThanThree(tableCards: List[Card]): P[Boolean] = (tableCards.length < 3).pure[P]
}

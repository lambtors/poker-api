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
    deckProvider: DeckProvider[P]
) {
  def add(gameId: GameId): P[Unit] =
    repository
      .search(gameId)
      .fold[P[Unit]](MonadErrorThrowable[P].raiseError(PokerGameNotFound(gameId)))(game =>
        thereAreMoreThanThreeCardsAtTable(game.tableCards).ifM(
          MonadErrorThrowable[P].raiseError(TurnNotPossibleWhenItIsAlreadyGiven(gameId)),
          thereAreLessThanThreeCardsAtTable(game.tableCards).ifM(
            MonadErrorThrowable[P].raiseError(TurnNotPossibleWhenFlopIsNotGiven(gameId)),
            playerRepository
              .search(gameId)
              .flatMap(
                players =>
                  shuffleAvailableCards(playersCards(players) ++ game.tableCards).flatMap(shuffledCards =>
                    repository.update(game.copy(tableCards = game.tableCards ++ shuffledCards.take(1))))
              )
          )
      ))
      .flatten

  private def shuffleAvailableCards(cardsInGame: List[Card]) =
    deckProvider
      .provide()
      .flatMap(cards => deckProvider.shuffleGivenDeck(cards.filterNot(card => cardsInGame.contains(card))))

  private def playersCards(players: List[Player]): List[Card] =
    players.flatMap(player => List(player.firstCard, player.secondCard))

  private def thereAreMoreThanThreeCardsAtTable(tableCards: List[Card]): P[Boolean] = (tableCards.length > 3).pure[P]

  private def thereAreLessThanThreeCardsAtTable(tableCards: List[Card]): P[Boolean] = (tableCards.length < 3).pure[P]
}

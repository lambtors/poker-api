package com.lambtors.poker_api.module.poker.application.river

import scala.collection.immutable

import cats.implicits._
import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.error.{
  PokerGameNotFound,
  RiverNotPossibleWhenItIsAlreadyGiven,
  RiverNotPossibleWhenTurnIsNotGiven
}
import com.lambtors.poker_api.module.poker.domain.model.{Card, GameId, Player}
import com.lambtors.poker_api.module.shared.domain.DeckProvider
import com.lambtors.poker_api.module.shared.domain.types.ThrowableTypeClasses.MonadErrorThrowable

class RiverCardAdder[P[_]: MonadErrorThrowable](
    repository: PokerGameRepository[P],
    playerRepository: PlayerRepository[P],
    deckProvider: DeckProvider[P]
) {
  def add(gameId: GameId): P[Unit] =
    repository
      .search(gameId)
      .fold[P[Unit]](MonadErrorThrowable[P].raiseError(PokerGameNotFound(gameId)))(game =>
        thereAreMoreThanFourCardsAtTable(game.tableCards).ifM(
          MonadErrorThrowable[P].raiseError(RiverNotPossibleWhenItIsAlreadyGiven(gameId)),
          thereAreLessThanFourCardsAtTable(game.tableCards).ifM(
            MonadErrorThrowable[P].raiseError(RiverNotPossibleWhenTurnIsNotGiven(gameId)),
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

  private def thereAreMoreThanFourCardsAtTable(tableCards: List[Card]): P[Boolean] = (tableCards.length > 4).pure[P]

  private def thereAreLessThanFourCardsAtTable(tableCards: List[Card]): P[Boolean] = (tableCards.length < 4).pure[P]
}

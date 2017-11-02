package com.lambtors.poker_api.module.poker.application.flop

import scala.concurrent.{ExecutionContext, Future}

import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.error.{FlopNotPossibleWhenItIsAlreadyGiven, PokerGameNotFound}
import com.lambtors.poker_api.module.poker.domain.model.{GameId, Player}
import com.lambtors.poker_api.module.shared.domain.DeckProvider

final class FlopCardsAdder(
    repository: PokerGameRepository,
    playerRepository: PlayerRepository,
    deckProvider: DeckProvider
)(implicit ec: ExecutionContext) {
  def add(gameId: GameId): Future[Unit] =
    repository
      .search(gameId)
      .flatMap(
        _.fold[Future[Unit]](Future.failed(PokerGameNotFound(gameId)))(game =>
          if (game.tableCards.nonEmpty) Future.failed(FlopNotPossibleWhenItIsAlreadyGiven(gameId))
          else {
            playerRepository
              .search(gameId)
              .flatMap(players =>
                repository.update(
                  game.copy(tableCards = deckProvider.shuffleGivenDeck(availableCards(players)).take(3))))
        }))

  private def availableCards(players: List[Player]) =
    deckProvider.provide().filterNot(card => playerCards(players).contains(card))

  private def playerCards(players: List[Player]) = players.flatMap(player => List(player.firstCard, player.secondCard))
}

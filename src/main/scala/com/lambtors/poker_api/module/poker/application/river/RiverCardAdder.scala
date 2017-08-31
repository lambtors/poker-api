package com.lambtors.poker_api.module.poker.application.river

import com.lambtors.poker_api.module.poker.domain.error.{
  PokerGameNotFound,
  RiverNotPossibleWhenItIsAlreadyGiven,
  RiverNotPossibleWhenTurnIsNotGiven
}
import com.lambtors.poker_api.module.poker.domain.model.{Card, GameId, Player}
import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.shared.domain.DeckProvider
import scala.concurrent.{ExecutionContext, Future}

class RiverCardAdder(
    repository: PokerGameRepository,
    playerRepository: PlayerRepository,
    deckProvider: DeckProvider
)(implicit ec: ExecutionContext) {
  def add(gameId: GameId): Future[Unit] =
    repository
      .search(gameId)
      .flatMap(
        _.fold[Future[Unit]](Future.failed(PokerGameNotFound(gameId)))(
          game =>
            if (game.tableCards.length > 4) Future.failed(RiverNotPossibleWhenItIsAlreadyGiven(gameId))
            else if (game.tableCards.length < 4) Future.failed(RiverNotPossibleWhenTurnIsNotGiven(gameId))
            else {
              playerRepository
                .search(gameId)
                .flatMap(
                  players =>
                    repository.update(
                      game.copy(
                        tableCards = game.tableCards ++ deckProvider
                          .shuffleGivenDeck(availableCards(playersCards(players) ++ game.tableCards))
                          .take(1))
                  )
                )
          }))

  private def availableCards(cardsInGame: List[Card]) =
    deckProvider.provide().filterNot(card => cardsInGame.contains(card))

  private def playersCards(players: List[Player]) =
    players.flatMap(player => List(player.firstCard, player.secondCard))
}

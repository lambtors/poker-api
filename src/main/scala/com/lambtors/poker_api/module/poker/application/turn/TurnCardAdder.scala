package com.lambtors.poker_api.module.poker.application.turn

import com.lambtors.poker_api.module.poker.domain.error.{
  PokerGameNotFound,
  TurnNotPossibleWhenFlopIsNotGiven,
  TurnNotPossibleWhenItIsAlreadyGiven
}
import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.model.{Card, GameId, Player}
import com.lambtors.poker_api.module.shared.domain.DeckProvider
import scala.concurrent.{ExecutionContext, Future}

final class TurnCardAdder(
    repository: PokerGameRepository[Future],
    playerRepository: PlayerRepository[Future],
    deckProvider: DeckProvider
)(implicit ec: ExecutionContext) {
  def add(gameId: GameId): Future[Unit] =
    repository
      .search(gameId)
      .flatMap(_.fold(Future.failed[Unit](PokerGameNotFound(gameId))) { game =>
        if (game.tableCards.length > 3) {
          Future.failed[Unit](TurnNotPossibleWhenItIsAlreadyGiven(gameId))
        } else if (game.tableCards.length < 3) {
          Future.failed[Unit](TurnNotPossibleWhenFlopIsNotGiven(gameId))
        } else {
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
        }
      })
  private def availableCards(cardsInGame: List[Card]) =
    deckProvider.provide().filterNot(card => cardsInGame.contains(card))

  private def playersCards(players: List[Player]) =
    players.flatMap(player => List(player.firstCard, player.secondCard))
}

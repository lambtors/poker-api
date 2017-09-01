package com.lambtors.poker_api.module.poker.application.river

import cats.implicits._
import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.error.{
  PokerGameNotFound,
  RiverNotPossibleWhenItIsAlreadyGiven,
  RiverNotPossibleWhenTurnIsNotGiven
}
import com.lambtors.poker_api.module.poker.domain.model.{Card, GameId, Player, PokerGame}
import com.lambtors.poker_api.module.shared.domain.DeckProvider
import com.lambtors.poker_api.module.shared.domain.types.ThrowableTypeClasses.MonadErrorThrowable

class RiverCardAdder[P[_]: MonadErrorThrowable](
    repository: PokerGameRepository[P],
    playerRepository: PlayerRepository[P],
    deckProvider: DeckProvider
) {
  def add(gameId: GameId): P[Unit] =
    repository
      .search(gameId)
      .flatMap(
        _.fold[P[Unit]](MonadErrorThrowable[P].raiseError(PokerGameNotFound(gameId)))(
          (game: PokerGame) =>
            if (game.tableCards.length > 4)
              MonadErrorThrowable[P].raiseError(RiverNotPossibleWhenItIsAlreadyGiven(gameId))
            else if (game.tableCards.length < 4)
              MonadErrorThrowable[P].raiseError(RiverNotPossibleWhenTurnIsNotGiven(gameId))
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

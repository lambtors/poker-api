package com.lambtors.poker_api.module.poker.application.river

import com.lambtors.poker_api.module.poker.domain.error.{
  PokerGameNotFound,
  RiverNotPossibleWhenItIsAlreadyGiven,
  RiverNotPossibleWhenTurnIsNotGiven
}
import com.lambtors.poker_api.module.poker.domain.model.GameId
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
      .map(
        gameOpt =>
          if (gameOpt.isEmpty) {
            throw PokerGameNotFound(gameId)
          } else {
            val game = gameOpt.get

            if (game.tableCards.length > 4) {
              throw RiverNotPossibleWhenItIsAlreadyGiven(gameId)
            } else if (game.tableCards.length < 4) {
              throw RiverNotPossibleWhenTurnIsNotGiven(gameId)
            } else {
              val cards = deckProvider.provide()

              playerRepository
                .search(gameId)
                .flatMap(
                  players => {
                    val playersCards                   = players.flatMap(player => List(player.firstCard, player.secondCard))
                    val playerCardsWithCardsAtTheTable = playersCards ++ game.tableCards

                    val availableCards = cards.filterNot(card => playerCardsWithCardsAtTheTable.contains(card))

                    repository.update(
                      game.copy(tableCards = game.tableCards ++ deckProvider.shuffleGivenDeck(availableCards).take(1))
                    )
                  }
                )
            }
        }
      )
}

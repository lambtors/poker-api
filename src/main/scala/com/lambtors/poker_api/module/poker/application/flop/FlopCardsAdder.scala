package com.lambtors.poker_api.module.poker.application.flop

import scala.concurrent.{ExecutionContext, Future}
import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.error.{FlopNotPossibleWhenItIsAlreadyGiven, PokerGameNotFound}
import com.lambtors.poker_api.module.poker.domain.model.GameId
import com.lambtors.poker_api.module.shared.domain.DeckProvider

final class FlopCardsAdder(
  repository: PokerGameRepository,
  playerRepository: PlayerRepository,
  deckProvider: DeckProvider
)(implicit ec: ExecutionContext) {
  def add(gameId: GameId): Future[Unit] = repository.search(gameId).map(
    gameOpt =>
      if (gameOpt.isEmpty) {
        throw PokerGameNotFound(gameId)
      } else {
        val game = gameOpt.get
        if (game.tableCards.nonEmpty) {
          throw FlopNotPossibleWhenItIsAlreadyGiven(gameId)
        } else {
          val cards = deckProvider.provide()

          playerRepository.search(gameId).flatMap(
            players => {
              val playersCards = players.flatMap(player => List(player.firstCard, player.secondCard))

              val availableCards = cards.filterNot(card => playersCards.contains(card))

              repository.update(game.copy(tableCards = deckProvider.shuffleGivenDeck(availableCards).take(3)))
            }
          )
        }
      }
  )
}

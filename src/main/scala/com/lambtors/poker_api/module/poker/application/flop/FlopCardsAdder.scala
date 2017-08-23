package com.lambtors.poker_api.module.poker.application.flop

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.error.PokerGameNotFound
import com.lambtors.poker_api.module.poker.domain.model.{Card, GameId}

final class FlopCardsAdder(repository: PokerGameRepository, playerRepository: PlayerRepository)
  (implicit ec: ExecutionContext) {
  def add(gameId: GameId): Future[Unit] = repository.search(gameId).map(
    gameOpt =>
      if (gameOpt.isEmpty) {
        throw PokerGameNotFound(gameId)
      } else {
        val game = gameOpt.get
        val cards = Random.shuffle(Card.allCards)

        playerRepository.search(gameId).flatMap(
          players => {
            val playersCards = players.flatMap(player => List(player.firstCard, player.secondCard))

            val availableCards = cards.filterNot(card => playersCards.contains(card))

            repository.update(game.flopFromAvailableCards(availableCards))
          }
        )
      }
  )
}

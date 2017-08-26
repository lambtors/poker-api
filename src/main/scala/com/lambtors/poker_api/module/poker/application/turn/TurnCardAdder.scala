package com.lambtors.poker_api.module.poker.application.turn

import com.lambtors.poker_api.module.poker.domain.error.{
  PokerGameNotFound,
  TurnNotPossibleWhenFlopIsNotGiven,
  TurnNotPossibleWhenItIsAlreadyGiven
}
import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.model.GameId
import com.lambtors.poker_api.module.shared.domain.DeckProvider

import scala.concurrent.{ExecutionContext, Future}

final class TurnCardAdder(
  repository: PokerGameRepository,
  playerRepository: PlayerRepository,
  deckProvider: DeckProvider
)(implicit ec: ExecutionContext) {
  def add(gameId: GameId): Future[Unit] = repository.search(gameId).map(
    gameOpt => if (gameOpt.isEmpty) {
      throw PokerGameNotFound(gameId)
    } else {
      val game = gameOpt.get

      if (game.tableCards.length > 3) {
        throw TurnNotPossibleWhenItIsAlreadyGiven(gameId)
      } else if (game.tableCards.length < 3) {
        throw TurnNotPossibleWhenFlopIsNotGiven(gameId)
      } else {
        val cards = deckProvider.provide()

        playerRepository.search(gameId).flatMap(
          players => {
            val playersCards = players.flatMap(player => List(player.firstCard, player.secondCard))
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

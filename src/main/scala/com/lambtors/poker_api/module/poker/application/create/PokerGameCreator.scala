package com.lambtors.poker_api.module.poker.application.create

import scala.concurrent.{ExecutionContext, Future}
import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.error.PokerGameAlreadyExisting
import com.lambtors.poker_api.module.poker.domain.model._
import com.lambtors.poker_api.module.shared.domain.{DeckProvider, UUIDProvider}

final class PokerGameCreator(
  pokerGameRepository: PokerGameRepository,
  playerRepository: PlayerRepository,
  uUIDProvider: UUIDProvider,
  deckProvider: DeckProvider
)(implicit ec: ExecutionContext) {
  def create(amountOfPlayers: AmountOfPlayers, gameId: GameId): Future[Unit] =
    pokerGameRepository.search(gameId).flatMap { searchResult =>
      if (searchResult.isDefined) {
        Future.failed[Unit](PokerGameAlreadyExisting(gameId))
      } else {
        pokerGameRepository.insert(PokerGame(gameId, amountOfPlayers, List.empty)).map(_ => {
          var cards = deckProvider.provide()

          for (_ <- 1 to amountOfPlayers.amount) {
            val firstCard :: cardsWithoutFirstCard = cards
            val secondCard :: cardsWithoutFirstAndSecondCard = cardsWithoutFirstCard
            cards = cardsWithoutFirstAndSecondCard
            playerRepository.insert(Player(PlayerId(uUIDProvider.provide()), gameId, firstCard, secondCard))
          }
        })
      }
    }
}

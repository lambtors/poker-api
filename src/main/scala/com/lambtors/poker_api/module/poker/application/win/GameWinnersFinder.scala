package com.lambtors.poker_api.module.poker.application.win

import scala.concurrent.{ExecutionContext, Future}

import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.error.{GameCannotEndWhenRiverIsNotDealt, PokerGameNotFound}
import com.lambtors.poker_api.module.poker.domain.model._

final class GameWinnersFinder(repository: PokerGameRepository, playerRepository: PlayerRepository)(
    implicit ec: ExecutionContext) {

  def findWinners(gameId: GameId): Future[List[Player]] =
    repository
      .search(gameId)
      .flatMap(
        _.fold[Future[List[Player]]](Future.failed(PokerGameNotFound(gameId)))(game =>
          if (game.tableCards.length < 5) {
            Future.failed(GameCannotEndWhenRiverIsNotDealt(gameId))
          } else {
            playerRepository.search(gameId).map { players =>
              findPlayersWithBestCombination(players, game.tableCards)
            }
        })
      )

  private def findPlayersWithBestCombination(players: List[Player], tableCards: List[Card]): List[Player] = {
    val bestCombinations = players.map(findBestCombinationOf(_, tableCards))
    val highestCardValueOutOfAllCombinations = bestCombinations
      .map(_.combination match {
        case HighCard(card) => card.cardValue
      })
      .sorted(CardValueOrdering.highestValueToLowest)
      .head
    val playerBestCombinationsWithHighestCardValue = bestCombinations.filter(_.combination match {
      case HighCard(card) => card.cardValue == highestCardValueOutOfAllCombinations
    })
    playerBestCombinationsWithHighestCardValue.map(_.player)
  }

  private def findBestCombinationOf(player: Player, tableCards: List[Card]): PlayerBestCombination = {
    // TODO add complex logic here to accept pairs, trios, pokers, flushes, etc as combinations
    val allCards = List(player.firstCard, player.secondCard) ++ tableCards
    val bestCard = allCards.sorted(CardOrdering.highestValueToLowest).head
    PlayerBestCombination(player, HighCard(bestCard))
  }
}

case class PlayerBestCombination(player: Player, combination: CardCombination)

sealed abstract class CardCombination
final case class HighCard(card: Card) extends CardCombination

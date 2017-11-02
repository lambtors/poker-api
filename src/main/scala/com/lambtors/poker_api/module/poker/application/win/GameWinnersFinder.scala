package com.lambtors.poker_api.module.poker.application.win

import cats.implicits._
import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.error.{GameCannotEndWhenRiverIsNotDealt, PokerGameNotFound}
import com.lambtors.poker_api.module.poker.domain.model._
import com.lambtors.poker_api.module.shared.domain.types.ThrowableTypeClasses.MonadErrorThrowable

final class GameWinnersFinder[P[_]: MonadErrorThrowable](repository: PokerGameRepository[P],
                                                         playerRepository: PlayerRepository[P]) {

  def findWinners(gameId: GameId): P[List[Player]] =
    repository
      .search(gameId)
      .flatMap(
        _.fold[P[List[Player]]](MonadErrorThrowable[P].raiseError(PokerGameNotFound(gameId)))(game =>
          cardsAtTableNumberIsLowerThanFive(game.tableCards).ifM(
            MonadErrorThrowable[P].raiseError(GameCannotEndWhenRiverIsNotDealt(gameId)),
            playerRepository.search(gameId).map(findPlayersWithBestCombination(_, game.tableCards))
        ))
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
    val bestCard =
      (List(player.firstCard, player.secondCard) ++ tableCards).sorted(CardOrdering.highestValueToLowest).head
    PlayerBestCombination(player, HighCard(bestCard))
  }

  private def cardsAtTableNumberIsLowerThanFive(tableCards: List[Card]): P[Boolean] = (tableCards.length < 5).pure[P]
}

case class PlayerBestCombination(player: Player, combination: CardCombination)

sealed abstract class CardCombination
final case class HighCard(card: Card) extends CardCombination

package com.lambtors.poker_api.module.poker.domain.model

import scala.util.Random

final case class PokerGame(gameId: GameId, amountOfPlayers: AmountOfPlayers, tableCards: List[Card]) {
  def flopFromAvailableCards(availableCards: List[Card]): PokerGame = copy(
    tableCards = takeRandomCards(3)(availableCards)
  )

  def turnFromAvailableCards(availableCards: List[Card]): PokerGame = copy(
    tableCards = takeRandomCards(1)(availableCards)
  )

  def riverFromAvailableCards(availableCards: List[Card]): PokerGame = copy(
    tableCards = takeRandomCards(1)(availableCards)
  )

  private def takeRandomCards(numOfCards: Int)(cards: List[Card]): List[Card] = Random.shuffle(cards).take(numOfCards)
}

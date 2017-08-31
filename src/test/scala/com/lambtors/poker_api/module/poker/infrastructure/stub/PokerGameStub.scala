package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.domain.model.{AmountOfPlayers, Card, GameId, PokerGame}

import scala.util.Random

object PokerGameStub {
  def create(gameId: GameId = GameIdStub.random(),
             amountOfPlayers: AmountOfPlayers = AmountOfPlayersStub.random(),
             tableCards: List[Card] = (0 to Random.nextInt(5)).map(_ => CardStub.random()).toList): PokerGame =
    PokerGame(gameId, amountOfPlayers, tableCards)

  def createNew(gameId: GameId = GameIdStub.random(),
                amountOfPlayers: AmountOfPlayers = AmountOfPlayersStub.random()): PokerGame =
    create(gameId, amountOfPlayers, List.empty)

  def createGameAtFlop(gameId: GameId = GameIdStub.random(),
                       amountOfPlayers: AmountOfPlayers = AmountOfPlayersStub.random()): PokerGame =
    create(gameId, amountOfPlayers, (1 to 3).map(_ => CardStub.random()).toList)

  def createGameAtTurn(gameId: GameId = GameIdStub.random(),
                       amountOfPlayers: AmountOfPlayers = AmountOfPlayersStub.random()): PokerGame =
    create(gameId, amountOfPlayers, (1 to 4).map(_ => CardStub.random()).toList)

  def createGameAtRiver(gameId: GameId = GameIdStub.random(),
                        amountOfPlayers: AmountOfPlayers = AmountOfPlayersStub.random()): PokerGame =
    create(gameId, amountOfPlayers, (1 to 5).map(_ => CardStub.random()).toList)

  def createGameNotAtFlop(gameId: GameId = GameIdStub.random(),
                          amountOfPlayers: AmountOfPlayers = AmountOfPlayersStub.random()): PokerGame =
    create(
      gameId,
      amountOfPlayers,
      (0 to randomNumberOfCardsExceptOne(2)).filter(_ != 2).map(_ => CardStub.random()).toList
    )

  def createGameNotAtTurn(gameId: GameId = GameIdStub.random(),
                          amountOfPlayers: AmountOfPlayers = AmountOfPlayersStub.random()): PokerGame =
    create(
      gameId,
      amountOfPlayers,
      (0 to randomNumberOfCardsExceptOne(2)).filter(_ != 3).map(_ => CardStub.random()).toList
    )

  def createGameNotAtRiver(gameId: GameId = GameIdStub.random(),
                           amountOfPlayers: AmountOfPlayers = AmountOfPlayersStub.random()): PokerGame =
    create(
      gameId,
      amountOfPlayers,
      (0 to randomNumberOfCardsExceptOne(2)).filter(_ != 4).map(_ => CardStub.random()).toList
    )

  def random: PokerGame = create()

  private def randomNumberOfCardsExceptOne(value: Int): Int =
    if (Random.nextInt(5) == value) randomNumberOfCardsExceptOne(value) else value
}

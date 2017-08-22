package com.lambtors.poker_api.module.poker.behaviour.create

import java.util.UUID

import com.lambtors.poker_api.module.poker.application.create.{CreatePokerGameCommandHandler, PokerGameCreator}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpec
import com.lambtors.poker_api.module.poker.domain.error.{InvalidAmountOfPlayers, InvalidGameId, PokerGameAlreadyExisting}
import com.lambtors.poker_api.module.poker.infrastructure.stub.{AmountOfPlayersStub, CreatePokerGameCommandStub, GameIdStub, PokerGameStub}

final class CreatePokerGameSpec extends PokerBehaviourSpec {

  private implicit val ec = scala.concurrent.ExecutionContext.global
  val commandHandler      = new CreatePokerGameCommandHandler(new PokerGameCreator(pokerGameRepository))

  "A CreatePokerGameCommandHandler" should {
    "create a poker game" in {
      val command = CreatePokerGameCommandStub.random()

      val gameId    = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.create(gameId, AmountOfPlayersStub.create(command.amountOfPlayers))

      shouldNotFindPokerGame(gameId)
      shouldInsertPokerGame(pokerGame)

      commandHandler.handle(command).futureValue
    }

    "return a failed future in case a game already exists with the same id" in {
      val command = CreatePokerGameCommandStub.random()

      val gameId    = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.create(gameId)

      shouldFindPokerGame(gameId, pokerGame)

      commandHandler.handle(command).failed.futureValue should ===(PokerGameAlreadyExisting(gameId))
    }

    "return a validation error on invalid amount of players" in {
      val command = CreatePokerGameCommandStub.create(amountOfPlayers = AmountOfPlayersStub.invalid())

      commandHandler.handle(command).failed.futureValue should ===(InvalidAmountOfPlayers(command.amountOfPlayers))
    }

    "return a validation error on invalid game id" in {
      val command = CreatePokerGameCommandStub.create(gameId = GameIdStub.invalid())

      commandHandler.handle(command).failed.futureValue should ===(InvalidGameId(command.gameId))
    }
  }
}

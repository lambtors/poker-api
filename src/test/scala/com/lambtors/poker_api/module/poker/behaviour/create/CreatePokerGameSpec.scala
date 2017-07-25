package com.lambtors.poker_api.module.poker.behaviour.create

import java.util.UUID

import com.lambtors.poker_api.module.poker.application.create.{CreatePokerGameCommandHandler, PokerGameCreator}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpec
import com.lambtors.poker_api.module.poker.infrastructure.stub.{
  AmountOfPlayersStub,
  CreatePokerGameCommandStub,
  GameIdStub,
  PokerGameStub
}

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
  }

}

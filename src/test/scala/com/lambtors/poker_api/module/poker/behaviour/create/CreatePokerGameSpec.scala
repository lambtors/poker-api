package com.lambtors.poker_api.module.poker.behaviour.create

import com.lambtors.poker_api.module.poker.application.create.{CreatePokerGameCommandHandler, PokerGameCreator}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpec
import com.lambtors.poker_api.module.poker.infrastructure.stub.CreatePokerGameCommandStub

final class CreatePokerGameSpec extends PokerBehaviourSpec {

  private implicit val ec = scala.concurrent.ExecutionContext.global
  val commandHandler      = new CreatePokerGameCommandHandler(new PokerGameCreator(pokerGameRepository))

  "A CreatePokerGameCommandHandler" should {
    "create a poker game" in {
      val command = CreatePokerGameCommandStub.random()

      commandHandler.handle(command).futureValue
    }
  }

}

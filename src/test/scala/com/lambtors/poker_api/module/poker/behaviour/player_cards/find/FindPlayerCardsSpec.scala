package com.lambtors.poker_api.module.poker.behaviour.player_cards.find

import java.util.UUID

import com.lambtors.poker_api.module.poker.application.player_cards.find.{
  FindPlayerCardsQueryHandler,
  PlayerCardsFinder
}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpec
import com.lambtors.poker_api.module.poker.infrastructure.stub.{
  FakePlayerIdStub,
  FakePlayerStub,
  FindPlayerCardsQueryStub,
  FindPlayerCardsResponseStub
}

final class FindPlayerCardsSpec extends PokerBehaviourSpec {

  val queryHandler = new FindPlayerCardsQueryHandler(new PlayerCardsFinder(fakePlayerRepository))

  "A FindPlayerCardsQueryHandler" should {
    "find player cards" in {
      val query = FindPlayerCardsQueryStub.random()

      val player = FakePlayerStub.create(id = FakePlayerIdStub.create(UUID.fromString(query.playerId)))
      shouldFindFakePlayer(player.id, player)

      val expectedResponse = FindPlayerCardsResponseStub.create(player.cards)

      queryHandler.handle(query).futureValue shouldBe expectedResponse
    }
  }
}

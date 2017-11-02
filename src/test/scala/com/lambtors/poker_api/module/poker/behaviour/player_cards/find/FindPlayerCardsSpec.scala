package com.lambtors.poker_api.module.poker.behaviour.player_cards.find

import java.util.UUID

import cats.implicits._
import com.lambtors.poker_api.module.poker.application.player_cards.find.{
  FindPlayerCardsQueryHandler,
  PlayerCardsFinder
}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpec
import com.lambtors.poker_api.module.poker.domain.error.PlayerNotFound
import com.lambtors.poker_api.module.poker.infrastructure.stub.{
  FindPlayerCardsQueryStub,
  FindPlayerCardsResponseStub,
  PlayerIdStub,
  PlayerStub
}

final class FindPlayerCardsSpec extends PokerBehaviourSpec {

  val queryHandler = new FindPlayerCardsQueryHandler(new PlayerCardsFinder(playerRepository))

  "A FindPlayerCardsQueryHandler" should {
    "find player cards" in {
      val query = FindPlayerCardsQueryStub.random()

      val player = PlayerStub.create(playerId = PlayerIdStub.create(UUID.fromString(query.playerId)))
      shouldFindPlayer(player.playerId, player)

      val expectedResponse = FindPlayerCardsResponseStub.create(player.cards)

      val result = queryHandler.handle(query)
      result should beValid
      result.map(_.futureValue should ===(expectedResponse))
    }

    "fail if the player does not exist" in {
      val query = FindPlayerCardsQueryStub.random()

      val playerId = PlayerIdStub.create(UUID.fromString(query.playerId))
      shouldNotFindPlayer(playerId)

      queryHandler.handle(query) should beFailedFutureWith(PlayerNotFound(playerId))
    }

    // TODO missing validation test
  }
}

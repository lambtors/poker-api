package com.lambtors.poker_api.module.poker.behaviour.player_cards.find

import java.util.UUID

import cats.implicits._
import com.lambtors.poker_api.module.poker.application.player_cards.find.{
  FindPlayerCardsQueryHandler,
  PlayerCardsFinder
}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpec
import com.lambtors.poker_api.module.poker.domain.error.{InvalidPlayerId, PlayerNotFound}
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

      val initialState = PokerState.empty.withPlayer(player)

      val expectedResponse = FindPlayerCardsResponseStub.create(player.cards)

      val validatedStateT = queryHandler.handle(query)
      validatedStateT should beValid
      validatedStateT.map(_.runA(initialState) should beRightContaining(expectedResponse))
    }

    "fail if the player does not exist" in {
      val query = FindPlayerCardsQueryStub.random()

      val playerId = PlayerIdStub.create(UUID.fromString(query.playerId))

      val initialState = PokerState.empty

      val validatedStateT = queryHandler.handle(query)
      validatedStateT should beValid
      validatedStateT.map(_.runA(initialState) should beLeftContaining[Throwable](PlayerNotFound(playerId)))
    }

    "have validation errors on an invalid query" in {
      val query = FindPlayerCardsQueryStub.invalid()

      queryHandler.handle(query) should haveValidationErrors(InvalidPlayerId(query.playerId))
    }
  }
}

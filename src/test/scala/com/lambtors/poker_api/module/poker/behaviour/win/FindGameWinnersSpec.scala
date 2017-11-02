package com.lambtors.poker_api.module.poker.behaviour.win

import java.util.UUID

import scala.concurrent.Future
import scala.util.Random

import cats.implicits._

import com.lambtors.poker_api.module.poker.application.win.{FindGameWinnersQueryHandler, GameWinnersFinder}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpec
import com.lambtors.poker_api.module.poker.domain.error.{
  GameCannotEndWhenRiverIsNotDealt,
  InvalidGameId,
  PokerGameNotFound
}
import com.lambtors.poker_api.module.poker.domain.model.Card
import com.lambtors.poker_api.module.poker.domain.model.CardValue.Ace
import com.lambtors.poker_api.module.poker.infrastructure.stub._

final class FindGameWinnersSpec extends PokerBehaviourSpec {

  val queryHandler =
    new FindGameWinnersQueryHandler[Future](new GameWinnersFinder(pokerGameRepository, playerRepository))

  "A FindGameWinnersQueryHandler" should {

    "tie all players when there is an ace on the table" in {
      val query = FindGameWinnersQueryStub.random()

      val gameId  = GameIdStub.create(UUID.fromString(query.gameId))
      val cards   = CardStub.create(cardValue = Ace) +: (1 to 4).map(_ => CardStub.random()).toList
      val game    = PokerGameStub.createGameAtRiver(cards = cards)
      val players = (0 to game.amountOfPlayers.amount).map(_ => PlayerStub.create(gameId = gameId)).toList

      shouldFindPokerGame(gameId, game)
      shouldFindPlayersByGameId(gameId, players)

      val expectedResponse = FindGameWinnersResponseStub.create(players.map(_.playerId.playerId.toString))

      val result = queryHandler.handle(query)
      result should beValid
      result.map(_.futureValue should ===(expectedResponse))
    }

    "acknowledge a single winner when he has the highest card" in {
      val query = FindGameWinnersQueryStub.random()

      val gameId = GameIdStub.create(UUID.fromString(query.gameId))
      val cards  = (1 to 5).map(_ => notAnAceCard()).toList
      val game   = PokerGameStub.createGameAtRiver(cards = cards)
      val winner = PlayerStub.create(firstCard = CardStub.create(cardValue = Ace))
      val losers = (1 to game.amountOfPlayers.amount)
        .map(_ => PlayerStub.create(gameId = gameId, firstCard = notAnAceCard(), secondCard = notAnAceCard()))
        .toList
      val players = Random.shuffle(winner +: losers)

      shouldFindPokerGame(gameId, game)
      shouldFindPlayersByGameId(gameId, players)

      val expectedResponse = FindGameWinnersResponseStub.create(List(winner.playerId.playerId.toString))

      val result = queryHandler.handle(query)
      result should beValid
      result.map(_.futureValue should ===(expectedResponse))
    }

    "fail if the game is not in end position" in {
      val query = FindGameWinnersQueryStub.random()

      val gameId = GameIdStub.create(UUID.fromString(query.gameId))
      val game   = PokerGameStub.createGameNotAtRiver()

      shouldFindPokerGame(gameId, game)

      queryHandler.handle(query) should beFailedFutureWith(GameCannotEndWhenRiverIsNotDealt(gameId))
    }

    "fail if the game doesn't exist" in {
      val query = FindGameWinnersQueryStub.random()

      val gameId = GameIdStub.create(UUID.fromString(query.gameId))

      shouldNotFindPokerGame(gameId)

      queryHandler.handle(query) should beFailedFutureWith(PokerGameNotFound(gameId))
    }

    "fail with validation errors when the query is invalid" in {
      val query = FindGameWinnersQueryStub.invalid()

      queryHandler.handle(query) should haveValidationErrors(InvalidGameId(query.gameId))
    }
  }

  def notAnAceCard(): Card = CardStub.create(cardValue = CardValueStub.notAce())
}

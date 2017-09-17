package com.lambtors.poker_api.module.poker.behaviour.create

import java.util.UUID

import cats.implicits._
import com.lambtors.poker_api.module.poker.application.create.{CreatePokerGameCommandHandler, PokerGameCreator}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpecT
import com.lambtors.poker_api.module.poker.infrastructure.stub._
import com.lambtors.poker_api.module.shared.ProviderSpec

final class CreatePokerGameSpec extends PokerBehaviourSpecT with ProviderSpec {
  val commandHandler = new CreatePokerGameCommandHandler[Q](
    new PokerGameCreator[Q](pokerGameRepository, playerRepository, uuidProviderStateT, deckProviderStateT)
  )

  "A CreatePokerGameCommandHandler" should {
    "create a poker game" in {
      var initialState: PokerState = PokerState.empty
      var finalState: PokerState   = PokerState.empty

      val command = CreatePokerGameCommandStub.random()

      val gameId    = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.createNew(gameId, AmountOfPlayersStub.create(command.amountOfPlayers))
      var deck      = CardStub.randomDeck()

      finalState = finalState.withGame(pokerGame)
      initialState = initialState.withDeck(deck)

      (1 to command.amountOfPlayers).foreach { _ =>
        val uuid = UUID.randomUUID()
        initialState = initialState.withUuid(uuid)

        val firstCard :: cardsWithoutFirstCard           = deck
        val secondCard :: cardsWithoutFirstAndSecondCard = cardsWithoutFirstCard
        deck = cardsWithoutFirstAndSecondCard

        finalState = finalState.withPlayer(PlayerStub.create(PlayerIdStub.create(uuid), gameId, firstCard, secondCard))
      }

      val validatedStateT = commandHandler.handle(command)
      validatedStateT should beValid
      validatedStateT.map(_.runS(initialState) should beRightContaining(finalState))
    }

//    "return a failed future in case a game already exists with the same id" in {
//      val command = CreatePokerGameCommandStub.random()
//
//      val gameId    = GameIdStub.create(UUID.fromString(command.gameId))
//      val pokerGame = PokerGameStub.create(gameId)
//
//      shouldFindPokerGame(gameId, pokerGame)
//
//      commandHandler.handle(command) should beFailedFutureWith(PokerGameAlreadyExisting(gameId))
//    }
//
//    "return a validation error on invalid amount of players" in {
//      val command = CreatePokerGameCommandStub.create(amountOfPlayers = AmountOfPlayersStub.invalid())
//
//      commandHandler.handle(command) should haveValidationErrors(InvalidAmountOfPlayers(command.amountOfPlayers))
//    }
//
//    "return a validation error on invalid game id" in {
//      val command = CreatePokerGameCommandStub.create(gameId = GameIdStub.invalid())
//
//      commandHandler.handle(command) should haveValidationErrors(InvalidGameId(command.gameId))
//    }
  }
}

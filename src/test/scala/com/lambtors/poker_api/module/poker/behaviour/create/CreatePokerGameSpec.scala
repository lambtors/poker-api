package com.lambtors.poker_api.module.poker.behaviour.create

import java.util.UUID

import cats.implicits._
import com.lambtors.poker_api.module.poker.application.create.{CreatePokerGameCommandHandler, PokerGameCreator}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpec
import com.lambtors.poker_api.module.poker.domain.error.{
  InvalidAmountOfPlayers,
  InvalidGameId,
  PokerGameAlreadyExisting
}
import com.lambtors.poker_api.module.poker.infrastructure.stub._

final class CreatePokerGameSpec extends PokerBehaviourSpec {
  val commandHandler = new CreatePokerGameCommandHandler(
    new PokerGameCreator(pokerGameRepository, playerRepository, uuidProvider, deckProvider)
  )

  "A CreatePokerGameCommandHandler" should {
    "create a poker game" in {
      val command                  = CreatePokerGameCommandStub.random()
      var initialState: PokerState = PokerState.empty
      var finalState: PokerState   = PokerState.empty

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

    "fail in case a game already exists with the same id" in {
      val command                  = CreatePokerGameCommandStub.random()
      var initialState: PokerState = PokerState.empty

      val gameId    = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.create(gameId)

      initialState = initialState.withGame(pokerGame)

      val validatedStateT = commandHandler.handle(command)
      validatedStateT should beValid
      validatedStateT.map(_.runS(initialState) should beLeftContaining[Throwable](PokerGameAlreadyExisting(gameId)))
    }

    "return validation errors on an invalid command" in {
      val command = CreatePokerGameCommandStub.invalid()

      commandHandler.handle(command) should haveValidationErrors(InvalidGameId(command.gameId),
                                                                 InvalidAmountOfPlayers(command.amountOfPlayers))
    }
  }
}

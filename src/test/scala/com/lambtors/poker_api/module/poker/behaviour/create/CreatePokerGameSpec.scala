package com.lambtors.poker_api.module.poker.behaviour.create

import java.util.UUID

import cats.implicits._
import com.lambtors.poker_api.module.poker.application.create.{CreatePokerGameCommandHandler, PokerGameCreator}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpecT
import com.lambtors.poker_api.module.poker.domain.model.{GameId, Player, PlayerId, PokerGame}
import com.lambtors.poker_api.module.poker.infrastructure.stub._
import com.lambtors.poker_api.module.shared.ProviderSpec
import com.lambtors.poker_api.module.shared.domain.Validation
import org.scalactic.Validation

final class CreatePokerGameSpec extends PokerBehaviourSpecT with ProviderSpec {
  val commandHandler = new CreatePokerGameCommandHandler[Q](
    new PokerGameCreator[Q](pokerGameRepository, playerRepository, uuidProvider, deckProvider)
  )

  "A CreatePokerGameCommandHandler" should {
    "create a poker game" in {
      val command = CreatePokerGameCommandStub.random()

      val gameId    = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.createNew(gameId, AmountOfPlayersStub.create(command.amountOfPlayers))
      var deck      = CardStub.randomDeck()

      shouldProvideDeck(deck)

      (1 to command.amountOfPlayers).foreach { _ =>
        val uuid = UUID.randomUUID()
        shouldProvideUUID(uuid)

        val firstCard :: cardsWithoutFirstCard           = deck
        val secondCard :: cardsWithoutFirstAndSecondCard = cardsWithoutFirstCard
        deck = cardsWithoutFirstAndSecondCard

//        shouldInsertPlayer(PlayerStub.create(PlayerIdStub.create(uuid), gameId, firstCard, secondCard))
      }

      val initialState             = PokerState.empty
      val transformationValidation = commandHandler.handle(command)

      transformationValidation should beValid
      transformationValidation.map { transformation =>
        val finalState: R[PokerState] = transformation.runS(initialState)
        finalState.isRight should ===(true)
        println(finalState.right.get)
      }
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

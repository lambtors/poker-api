package com.lambtors.poker_api.module.poker.behaviour.create

import java.util.UUID

import com.lambtors.poker_api.module.poker.application.create.{CreatePokerGameCommandHandler, PokerGameCreator}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpec
import com.lambtors.poker_api.module.poker.domain.error.PokerGameAlreadyExisting
import com.lambtors.poker_api.module.poker.domain.model.{InvalidAmountOfPlayers, InvalidGameId}
import com.lambtors.poker_api.module.poker.infrastructure.stub._
import com.lambtors.poker_api.module.shared.ProviderSpec

final class CreatePokerGameSpec extends PokerBehaviourSpec with ProviderSpec {
  private val commandHandler = new CreatePokerGameCommandHandler(
    new PokerGameCreator(pokerGameRepository, playerRepository, uuidProvider, deckProvider)
  )

  "A CreatePokerGameCommandHandler" should {
    "create a poker game" in {
      val command = CreatePokerGameCommandStub.random()

      val gameId    = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.createNew(gameId, AmountOfPlayersStub.create(command.amountOfPlayers))
      var deck = CardStub.randomDeck()

      shouldNotFindPokerGame(gameId)
      shouldInsertPokerGame(pokerGame)
      shouldProvideDeck(deck)

      (1 to command.amountOfPlayers).foreach {_ =>
        val uuid = UUID.randomUUID()
        shouldProvideUUID(uuid)

        val firstCard :: cardsWithoutFirstCard = deck
        val secondCard :: cardsWithoutFirstAndSecondCard = cardsWithoutFirstCard
        deck = cardsWithoutFirstAndSecondCard

        shouldInsertPlayer(PlayerStub.create(PlayerIdStub.create(uuid), gameId, firstCard, secondCard))
      }

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

package com.lambtors.poker_api.module.poker.behaviour.turn

import java.util.UUID

import com.lambtors.poker_api.module.poker.application.turn.{AddTurnCardToTableCommandHandler, TurnCardAdder}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpec
import com.lambtors.poker_api.module.poker.domain.error.{PokerGameNotFound, TurnNotPossibleWhenFlopIsNotGiven, TurnNotPossibleWhenItIsAlreadyGiven}
import com.lambtors.poker_api.module.poker.domain.model.InvalidGameId
import com.lambtors.poker_api.module.poker.infrastructure.stub._
import com.lambtors.poker_api.module.shared.ProviderSpec

import scala.util.Random

class AddTurnCardToTableSpec extends PokerBehaviourSpec with ProviderSpec {
  private val commandHandler = new AddTurnCardToTableCommandHandler(
    new TurnCardAdder(pokerGameRepository, playerRepository, deckProvider)
  )

  "Add turn card to table command handler" should {
    "add a new card to table when flop is already given" in {
      val command = AddTurnCardToTableCommandStub.random()
      val gameId = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.createGameAtFlop(gameId)
      val deck = CardStub.randomDeck()
      val players = (0 to pokerGame.amountOfPlayers.amount).map(_ => PlayerStub.create(gameId = gameId)).toList
      val playersCards = players.flatMap(player => List(player.firstCard, player.secondCard))
      val playersCardsWithTableCards = playersCards ++ pokerGame.tableCards

      val availableCards = deck.filterNot(deckCard => playersCardsWithTableCards.contains(deckCard))
      val shuffledAvailableCards = Random.shuffle(availableCards)

      shouldFindPokerGame(gameId, pokerGame)
      shouldProvideDeck(deck)
      shouldFindPlayersByGameId(gameId, players)
      shouldShuffleGivenDeck(availableCards, shuffledAvailableCards)
      shouldUpdatePokerGame(pokerGame.copy(tableCards = pokerGame.tableCards ++ shuffledAvailableCards.take(1)))

      commandHandler.handle(command) should beSuccessfulFuture
    }

    "return a failed future in case the game has turn already given" in {
      val command = AddTurnCardToTableCommandStub.random()
      val gameId  = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.createGameAtTurn(gameId)

      shouldFindPokerGame(gameId, pokerGame)

      commandHandler.handle(command) should beFailedFutureWith(TurnNotPossibleWhenItIsAlreadyGiven(gameId))
    }

    "return a failed future in case the flop is not given yet" in {
      val command = AddTurnCardToTableCommandStub.random()
      val gameId  = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.createNew(gameId)

      shouldFindPokerGame(gameId, pokerGame)

      commandHandler.handle(command) should beFailedFutureWith(TurnNotPossibleWhenFlopIsNotGiven(gameId))
    }

    "return a failed future in case a game already exists with the same id" in {
      val command = AddTurnCardToTableCommandStub.random()
      val gameId  = GameIdStub.create(UUID.fromString(command.gameId))

      shouldNotFindPokerGame(gameId)

      commandHandler.handle(command) should beFailedFutureWith(PokerGameNotFound(gameId))
    }

    "return a validation error on invalid game id" in {
      val command = AddTurnCardToTableCommandStub.create(gameId = GameIdStub.invalid())

      commandHandler.handle(command) should haveValidationErrors(InvalidGameId(command.gameId))
    }
  }
}

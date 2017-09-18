package com.lambtors.poker_api.module.poker.behaviour.river

import java.util.UUID

import scala.util.Random

import cats.implicits._
import com.lambtors.poker_api.module.poker.application.river.{AddRiverCardToTableCommandHandler, RiverCardAdder}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpecT
import com.lambtors.poker_api.module.poker.domain.error.{
  InvalidGameId,
  PokerGameNotFound,
  RiverNotPossibleWhenItIsAlreadyGiven,
  RiverNotPossibleWhenTurnIsNotGiven
}
import com.lambtors.poker_api.module.poker.infrastructure.stub._

final class AddRiverCardToTableSpec extends PokerBehaviourSpecT {
  val commandHandler = new AddRiverCardToTableCommandHandler(
    new RiverCardAdder(pokerGameRepository, playerRepository, deckProviderStateT)
  )

  "Add river card to table spec" should {
    "add a new card to table when turn is already given" in {
      val command = AddRiverCardToTableCommandStub.random()

      val gameId                     = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame                  = PokerGameStub.createGameAtTurn(gameId)
      val deck                       = CardStub.randomDeck()
      val players                    = (0 to pokerGame.amountOfPlayers.amount).map(_ => PlayerStub.create(gameId = gameId)).toList
      val playersCards               = players.flatMap(player => List(player.firstCard, player.secondCard))
      val playersCardsWithTableCards = playersCards ++ pokerGame.tableCards
      val availableCards             = deck.filterNot(deckCard => playersCardsWithTableCards.contains(deckCard))
      val shuffledAvailableCards     = Random.shuffle(availableCards)

      val initialState =
        PokerState.empty
          .withGame(pokerGame)
          .withDeck(deck)
          .withPlayers(players)
          .withDeck(shuffledAvailableCards)
      val finalState =
        initialState
          .withGame(pokerGame.copy(tableCards = pokerGame.tableCards ++ shuffledAvailableCards.take(1)))
          .withoutDecks()

      val validatedStateT = commandHandler.handle(command)
      validatedStateT should beValid
      validatedStateT.map(_.runS(initialState) should beRightContaining(finalState))
    }

    "return a failed future in case the game has river already given" in {
      val command   = AddRiverCardToTableCommandStub.random()
      val gameId    = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.createGameAtRiver(gameId)

      val initialState = PokerState.empty.withGame(pokerGame)

      val validatedStateT = commandHandler.handle(command)
      validatedStateT should beValid
      validatedStateT.map(
        _.runS(initialState) should beLeftContaining[Throwable](RiverNotPossibleWhenItIsAlreadyGiven(gameId)))
    }

    "return a failed future in case the turn is not given yet" in {
      val command   = AddRiverCardToTableCommandStub.random()
      val gameId    = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.createGameAtFlop(gameId)

      val initialState = PokerState.empty.withGame(pokerGame)

      val validatedStateT = commandHandler.handle(command)
      validatedStateT should beValid
      validatedStateT.map(
        _.runS(initialState) should beLeftContaining[Throwable](RiverNotPossibleWhenTurnIsNotGiven(gameId)))
    }

    "return a failed future in case a game already exists with the same id" in {
      val command = AddRiverCardToTableCommandStub.random()
      val gameId  = GameIdStub.create(UUID.fromString(command.gameId))

      val validatedStateT = commandHandler.handle(command)
      validatedStateT should beValid
      validatedStateT.map(_.runS(PokerState.empty) should beLeftContaining[Throwable](PokerGameNotFound(gameId)))
    }

    "return a validation error on invalid game id" in {
      val command = AddRiverCardToTableCommandStub.create(gameId = GameIdStub.invalid())

      commandHandler.handle(command) should haveValidationErrors(InvalidGameId(command.gameId))
    }
  }
}

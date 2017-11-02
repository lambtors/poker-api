package com.lambtors.poker_api.module.poker.behaviour.turn

import java.util.UUID

import scala.util.Random

import cats.implicits._
import com.lambtors.poker_api.module.poker.application.turn.{AddTurnCardToTableCommandHandler, TurnCardAdder}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpec
import com.lambtors.poker_api.module.poker.domain.error.{
  InvalidGameId,
  PokerGameNotFound,
  TurnNotPossibleWhenFlopIsNotGiven,
  TurnNotPossibleWhenItIsAlreadyGiven
}
import com.lambtors.poker_api.module.poker.infrastructure.stub._

final class AddTurnCardToTableSpec extends PokerBehaviourSpec {
  val commandHandler = new AddTurnCardToTableCommandHandler(
    new TurnCardAdder(pokerGameRepository, playerRepository, deckProvider)
  )

  "Add turn card to table command handler" should {
    "add a new card to table when flop is already given" in {
      val command = AddTurnCardToTableCommandStub.random()

      val gameId                     = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame                  = PokerGameStub.createGameAtFlop(gameId)
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

    "fail in case the game has turn already given" in {
      val command   = AddTurnCardToTableCommandStub.random()
      val gameId    = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.createGameAtTurn(gameId)

      val initialState = PokerState.empty.withGame(pokerGame)

      val validatedStateT = commandHandler.handle(command)
      validatedStateT should beValid
      validatedStateT.map(
        _.runS(initialState) should beLeftContaining[Throwable](TurnNotPossibleWhenItIsAlreadyGiven(gameId)))
    }

    "fail in case the flop is not given yet" in {
      val command   = AddTurnCardToTableCommandStub.random()
      val gameId    = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.createNew(gameId)

      val initialState = PokerState.empty.withGame(pokerGame)

      val validatedStateT = commandHandler.handle(command)
      validatedStateT should beValid
      validatedStateT.map(
        _.runS(initialState) should beLeftContaining[Throwable](TurnNotPossibleWhenFlopIsNotGiven(gameId)))
    }

    "fail in case a game already exists with the same id" in {
      val command = AddTurnCardToTableCommandStub.random()
      val gameId  = GameIdStub.create(UUID.fromString(command.gameId))

      val validatedStateT = commandHandler.handle(command)
      validatedStateT should beValid
      validatedStateT.map(_.runS(PokerState.empty) should beLeftContaining[Throwable](PokerGameNotFound(gameId)))
    }

    "return a validation error on invalid game id" in {
      val command = AddTurnCardToTableCommandStub.create(gameId = GameIdStub.invalid())

      commandHandler.handle(command) should haveValidationErrors(InvalidGameId(command.gameId))
    }
  }
}

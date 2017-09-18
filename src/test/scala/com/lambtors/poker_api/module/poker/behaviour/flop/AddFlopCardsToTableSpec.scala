package com.lambtors.poker_api.module.poker.behaviour.flop

import java.util.UUID

import scala.util.Random

import cats.implicits._
import com.lambtors.poker_api.module.poker.application.flop.{AddFlopCardsToTableCommandHandler, FlopCardsAdder}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpecT
import com.lambtors.poker_api.module.poker.domain.error.{
  FlopNotPossibleWhenItIsAlreadyGiven,
  InvalidGameId,
  PokerGameNotFound
}
import com.lambtors.poker_api.module.poker.infrastructure.stub._

final class AddFlopCardsToTableSpec extends PokerBehaviourSpecT {
  val commandHandler = new AddFlopCardsToTableCommandHandler(
    new FlopCardsAdder(pokerGameRepository, playerRepository, deckProviderStateT)
  )

  "Add flop cards to table command hander" should {
    "add three cards to a table that does not have any card at the table" in {
      val command = AddFlopCardsToTableCommandStub.random()

      val gameId       = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame    = PokerGameStub.createNew(gameId)
      val deck         = CardStub.randomDeck()
      val players      = (0 to pokerGame.amountOfPlayers.amount).map(_ => PlayerStub.create(gameId = gameId)).toList
      val playersCards = players.flatMap(player => List(player.firstCard, player.secondCard))

      val availableCards         = deck.filterNot(deckCard => playersCards.contains(deckCard))
      val shuffledAvailableCards = Random.shuffle(availableCards)

      val initialState = PokerState.empty
        .withGame(pokerGame)
        .withDeck(deck)
        .withPlayers(players)
        .withDeck(shuffledAvailableCards)
      val finalState = initialState
        .withGame(pokerGame.copy(tableCards = shuffledAvailableCards.take(3)))
        .withoutDecks()

      val validatedStateT = commandHandler.handle(command)
      validatedStateT should beValid
      validatedStateT.map(_.runS(initialState) should beRightContaining(finalState))
    }

    "fail in case the game has flop already given" in {
      val command      = AddFlopCardsToTableCommandStub.random()
      var initialState = PokerState.empty

      val gameId    = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.createGameAtFlop(gameId)

      initialState = initialState.withGame(pokerGame)

      val validatedStateT = commandHandler.handle(command)
      validatedStateT should beValid
      validatedStateT.map(
        _.runS(initialState) should beLeftContaining[Throwable](FlopNotPossibleWhenItIsAlreadyGiven(gameId)))
    }

    "fail in case a game already exists with the same id" in {
      val command = AddFlopCardsToTableCommandStub.random()

      val gameId = GameIdStub.create(UUID.fromString(command.gameId))

      val validatedStateT = commandHandler.handle(command)
      validatedStateT should beValid
      validatedStateT.map(_.runS(PokerState.empty) should beLeftContaining[Throwable](PokerGameNotFound(gameId)))
    }

    "return a validation error on invalid game id" in {
      val command = AddFlopCardsToTableCommandStub.create(gameId = GameIdStub.invalid())

      commandHandler.handle(command) should haveValidationErrors(InvalidGameId(command.gameId))
    }
  }
}

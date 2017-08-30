package com.lambtors.poker_api.module.poker.behaviour.flop

import java.util.UUID

import com.lambtors.poker_api.module.poker.application.flop.{AddFlopCardsToTableCommandHandler, FlopCardsAdder}
import com.lambtors.poker_api.module.poker.behaviour.PokerBehaviourSpec
import com.lambtors.poker_api.module.poker.domain.error.{FlopNotPossibleWhenItIsAlreadyGiven, PokerGameNotFound}
import com.lambtors.poker_api.module.poker.domain.model.InvalidGameId
import com.lambtors.poker_api.module.poker.infrastructure.stub._
import com.lambtors.poker_api.module.shared.ProviderSpec

import scala.util.Random

class AddFlopCardsToTableSpec extends PokerBehaviourSpec with ProviderSpec {
  private val commandHandler = new AddFlopCardsToTableCommandHandler(
    new FlopCardsAdder(pokerGameRepository, playerRepository, deckProvider)
  )

  "Add flop cards to table command hander" should {
    "add three cards to a table that does not have any card at the table" in {
      val command      = AddFlopCardsToTableCommandStub.random()
      val gameId       = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame    = PokerGameStub.createNew(gameId)
      val deck         = CardStub.randomDeck()
      val players      = (0 to pokerGame.amountOfPlayers.amount).map(_ => PlayerStub.create(gameId = gameId)).toList
      val playersCards = players.flatMap(player => List(player.firstCard, player.secondCard))

      val availableCards         = deck.filterNot(deckCard => playersCards.contains(deckCard))
      val shuffledAvailableCards = Random.shuffle(availableCards)

      shouldFindPokerGame(gameId, pokerGame)
      shouldProvideDeck(deck)
      shouldFindPlayersByGameId(gameId, players)
      shouldShuffleGivenDeck(availableCards, shuffledAvailableCards)
      shouldUpdatePokerGame(pokerGame.copy(tableCards = shuffledAvailableCards.take(3)))

      commandHandler.handle(command).futureValue
    }

    "return a failed future in case the game has flop already given" in {
      val command   = AddFlopCardsToTableCommandStub.random()
      val gameId    = GameIdStub.create(UUID.fromString(command.gameId))
      val pokerGame = PokerGameStub.createGameAtFlop(gameId)

      shouldFindPokerGame(gameId, pokerGame)

      commandHandler.handle(command).failed.futureValue should ===(FlopNotPossibleWhenItIsAlreadyGiven(gameId))
    }

    "return a failed future in case a game already exists with the same id" in {
      val command = AddFlopCardsToTableCommandStub.random()
      val gameId  = GameIdStub.create(UUID.fromString(command.gameId))

      shouldNotFindPokerGame(gameId)

      commandHandler.handle(command).failed.futureValue should ===(PokerGameNotFound(gameId))
    }

    "return a validation error on invalid game id" in {
      val command = AddFlopCardsToTableCommandStub.create(gameId = GameIdStub.invalid())

      commandHandler.handle(command).failed.futureValue should ===(InvalidGameId(command.gameId))
    }
  }
}

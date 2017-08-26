package com.lambtors.poker_api.module.shared

import java.util.UUID

import com.lambtors.poker_api.module.poker.domain.model.Card
import com.lambtors.poker_api.module.shared.domain.{DeckProvider, UUIDProvider}
import org.scalamock.scalatest.MockFactory

trait ProviderSpec extends MockFactory {
  protected val uuidProvider = mock[UUIDProvider]
  protected val deckProvider = mock[DeckProvider]

  def shouldProvideUUID(uuid: UUID): Unit = (uuidProvider.provide _).expects().once().returns(uuid)
  def shouldProvideDeck(deck: List[Card]): Unit = (deckProvider.provide _).expects().once().returns(deck)
  def shouldShuffleGivenDeck(orderedDeck: List[Card], shuffledDeck: List[Card]): Unit =
    (deckProvider.shuffleGivenDeck _).expects(orderedDeck).once().returns(shuffledDeck)
}

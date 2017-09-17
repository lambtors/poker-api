package com.lambtors.poker_api.module.shared

import java.util.UUID

import scala.concurrent.Future

import com.lambtors.poker_api.module.poker.domain.model.Card
import com.lambtors.poker_api.module.shared.domain.{DeckProvider, UUIDProvider}
import org.scalamock.scalatest.MockFactory
import org.scalatest.OneInstancePerTest

trait ProviderSpec extends MockFactory with OneInstancePerTest {
  protected val uuidProvider = mock[UUIDProvider[Future]]
  protected val deckProvider = mock[DeckProvider[Future]]

  def shouldProvideUUID(uuid: UUID): Unit = (uuidProvider.provide _).expects().once().returns(Future.successful(uuid))
  def shouldProvideDeck(deck: List[Card]): Unit =
    (deckProvider.provide _).expects().once().returns(Future.successful(deck))
  def shouldShuffleGivenDeck(orderedDeck: List[Card], shuffledDeck: List[Card]): Unit =
    (deckProvider.shuffleGivenDeck _).expects(orderedDeck).once().returns(Future.successful(shuffledDeck))
}

package com.lambtors.poker_api.module.shared.infrastructure.provider

import scala.concurrent.Future

import com.lambtors.poker_api.module.poker.domain.model.Card
import com.lambtors.poker_api.module.shared.domain.DeckProvider
import scala.util.Random

final class ShuffledDeckProvider extends DeckProvider[Future] {
  override def provide(): Future[List[Card]]                          = Future.successful(Random.shuffle(Card.allCards))
  override def shuffleGivenDeck(deck: List[Card]): Future[List[Card]] = Future.successful(Random.shuffle(deck))
}

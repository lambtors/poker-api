package com.lambtors.poker_api.module.shared.infrastructure.provider

import com.lambtors.poker_api.module.poker.domain.model.Card
import com.lambtors.poker_api.module.shared.domain.DeckProvider

import scala.util.Random

class ShuffledDeckProvider extends DeckProvider {
  override def provide(): List[Card] = Random.shuffle(Card.allCards)
  override def shuffleGivenDeck(deck: List[Card]): List[Card] = Random.shuffle(deck)
}

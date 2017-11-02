package com.lambtors.poker_api.module.shared.domain

import com.lambtors.poker_api.module.poker.domain.model.Card

trait DeckProvider[P[_]] {
  def provide(): P[List[Card]]
  def shuffleGivenDeck(deck: List[Card]): P[List[Card]]
}

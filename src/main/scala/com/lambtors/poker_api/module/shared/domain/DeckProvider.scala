package com.lambtors.poker_api.module.shared.domain

import com.lambtors.poker_api.module.poker.domain.model.Card

trait DeckProvider {
  def provide(): List[Card]
  def shuffleGivenDeck(deck: List[Card]): List[Card]
}

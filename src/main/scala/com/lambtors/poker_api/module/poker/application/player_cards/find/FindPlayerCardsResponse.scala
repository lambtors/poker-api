package com.lambtors.poker_api.module.poker.application.player_cards.find

import com.lambtors.poker_api.module.poker.domain.model.Card

final case class FindPlayerCardsResponse(cards: (Card, Card))

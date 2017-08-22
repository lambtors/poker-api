package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.application.player_cards.find.FakePlayer
import com.lambtors.poker_api.module.poker.domain.model.{Card, FakePlayerId}

object FakePlayerStub {
  def create(id: FakePlayerId = FakePlayerIdStub.random(),
             cards: List[Card] = ListStub.randomElements(() => CardStub.random())): FakePlayer =
    FakePlayer(id, cards)

  def random: FakePlayer = create()
}

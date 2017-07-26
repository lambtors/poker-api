package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.domain.model.AmountOfPlayers

object AmountOfPlayersStub {
  def create(amountOfPlayers: Int): AmountOfPlayers =
    AmountOfPlayers(amountOfPlayers)

  def random(): AmountOfPlayers = create(NumberStub.randomBetweenInclusive(2, 9))

  def invalid(): Int = RandomElementStub.oneOf(0, 1, 10, 11)
}

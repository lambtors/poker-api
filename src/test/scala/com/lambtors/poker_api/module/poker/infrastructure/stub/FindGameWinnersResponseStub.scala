package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.application.win.FindGameWinnersResponse

object FindGameWinnersResponseStub {
  def create(winnersPlayerIds: List[String] = Nil): FindGameWinnersResponse =
    FindGameWinnersResponse(winnersPlayerIds)

  def random: FindGameWinnersResponse = create()
}

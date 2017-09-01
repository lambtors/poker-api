package com.lambtors.poker_api.module.poker.application.win

import com.lambtors.poker_api.infrastructure.query_bus.Query

final case class FindGameWinnersQuery(gameId: String) extends Query

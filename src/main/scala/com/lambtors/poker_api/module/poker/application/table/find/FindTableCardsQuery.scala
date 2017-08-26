package com.lambtors.poker_api.module.poker.application.table.find

import com.lambtors.poker_api.infrastructure.query_bus.Query

case class FindTableCardsQuery(gameId: String) extends Query

package com.lambtors.poker_api.module.poker.application.player_cards.find

import com.lambtors.poker_api.infrastructure.query_bus.Query

final case class FindPlayerCardsQuery(playerId: String) extends Query

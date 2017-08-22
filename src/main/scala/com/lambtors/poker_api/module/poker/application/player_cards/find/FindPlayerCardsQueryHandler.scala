package com.lambtors.poker_api.module.poker.application.player_cards.find

import scala.concurrent.Future

import com.lambtors.poker_api.infrastructure.query_bus.QueryHandler
import com.lambtors.poker_api.module.poker.domain.model.FakePlayerId

final class FindPlayerCardsQueryHandler(playerCardsFinder: PlayerCardsFinder)
    extends QueryHandler[FindPlayerCardsQuery, FindPlayerCardsResponse] {

  override def handle(query: FindPlayerCardsQuery): Future[FindPlayerCardsResponse] =
    validate(query).flatMap(playerCardsFinder.find(_))

  def validate(query: FindPlayerCardsQuery): Future[FakePlayerId] = FakePlayerId.fromString(query.playerId)
}

package com.lambtors.poker_api.module.poker.application.player_cards.find

import scala.concurrent.{ExecutionContext, Future}

import com.lambtors.poker_api.module.poker.domain.error.FakePlayerNotFound
import com.lambtors.poker_api.module.poker.domain.model.{Card, FakePlayerId}

final class PlayerCardsFinder(fakePlayerRepository: FakePlayerRepository)(implicit ec: ExecutionContext) {
  def find(playerId: FakePlayerId): Future[List[Card]] = fakePlayerRepository.find(playerId).flatMap { playerOption =>
    if (playerOption.isDefined) {
      Future.successful(playerOption.get.cards)
    } else {
      Future.failed(FakePlayerNotFound(playerId))
    }
  }
}

// Use the real ones
trait FakePlayerRepository {
  def find(id: FakePlayerId): Future[Option[FakePlayer]]
}

case class FakePlayer(id: FakePlayerId, cards: List[Card])

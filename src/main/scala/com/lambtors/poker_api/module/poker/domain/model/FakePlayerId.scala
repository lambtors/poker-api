package com.lambtors.poker_api.module.poker.domain.model

import java.util.UUID

import scala.concurrent.Future

import com.lambtors.poker_api.module.poker.domain.error.InvalidPlayerId
import com.lambtors.poker_api.module.poker.domain.model.validator.UuidValidator

final case class FakePlayerId(playerId: UUID)

object FakePlayerId {
  def fromString(possiblePlayerId: String): Future[FakePlayerId] =
    if (isValid(possiblePlayerId)) Future.successful(FakePlayerId(UUID.fromString(possiblePlayerId)))
    else Future.failed(InvalidPlayerId(possiblePlayerId))

  private def isValid(possiblePlayerId: String): Boolean = UuidValidator.isValid(possiblePlayerId)
}

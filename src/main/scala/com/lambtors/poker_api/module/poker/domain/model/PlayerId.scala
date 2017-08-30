package com.lambtors.poker_api.module.poker.domain.model

import java.util.UUID

import scala.concurrent.Future

import com.lambtors.poker_api.module.poker.domain.error.InvalidPlayerId
import com.lambtors.poker_api.module.poker.domain.model.validator.UuidValidator

final case class PlayerId(playerId: UUID)

object PlayerId {
  def fromString(possiblePlayerId: String): Future[PlayerId] =
    if (isValid(possiblePlayerId)) Future.successful(PlayerId(UUID.fromString(possiblePlayerId)))
    else Future.failed(InvalidPlayerId(possiblePlayerId))

  private def isValid(possiblePlayerId: String): Boolean = UuidValidator.isValid(possiblePlayerId)
}

package com.lambtors.poker_api.module.poker.domain.model

import java.util.UUID

import scala.concurrent.Future

import com.lambtors.poker_api.module.poker.domain.error.InvalidGameId
import com.lambtors.poker_api.module.poker.domain.model.validator.UuidValidator

final case class GameId(gameId: UUID)

object GameId {
  def fromString(possibleGameId: String): Future[GameId] =
    if (isValid(possibleGameId)) Future.successful(GameId(UUID.fromString(possibleGameId)))
    else Future.failed(InvalidGameId(possibleGameId))

  private def isValid(possibleGameId: String): Boolean = UuidValidator.isValid(possibleGameId)
}

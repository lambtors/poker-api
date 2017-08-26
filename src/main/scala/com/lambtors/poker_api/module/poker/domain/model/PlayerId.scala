package com.lambtors.poker_api.module.poker.domain.model

import java.util.UUID

import scala.concurrent.Future
import scala.util.matching.Regex

final case class PlayerId(playerId: UUID)

object PlayerId {
  private val uuidRegex: Regex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f‌​]{4}-[0-9a-f]{12}$".r

  def fromString(possiblePlayerId: String): Future[PlayerId] =
    if (isValid(possiblePlayerId)) Future.successful(PlayerId(UUID.fromString(possiblePlayerId)))
    else Future.failed(InvalidPlayerId(possiblePlayerId))

  private def isValid(possiblePlayerId: String): Boolean = uuidRegex.findFirstMatchIn(possiblePlayerId).isDefined
}

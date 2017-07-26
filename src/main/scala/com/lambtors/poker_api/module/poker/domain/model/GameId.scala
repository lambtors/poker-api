package com.lambtors.poker_api.module.poker.domain.model

import java.util.UUID

import scala.concurrent.Future
import scala.util.matching.Regex

final case class GameId(gameId: UUID)

object GameId {
  private val uuidRegex: Regex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f‌​]{4}-[0-9a-f]{12}$".r

  def fromString(possibleGameId: String): Future[GameId] =
    if (isValid(possibleGameId)) Future.successful(GameId(UUID.fromString(possibleGameId)))
    else Future.failed(InvalidGameId(possibleGameId))

  private def isValid(possibleGameId: String): Boolean = uuidRegex.findFirstMatchIn(possibleGameId).isDefined
}

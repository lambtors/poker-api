package com.lambtors.poker_api.module.poker.domain.model

import java.util.UUID

import scala.util.matching.Regex

import cats.data.Validated.{Invalid, Valid}
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final case class GameId(gameId: UUID)

object GameId {
  private val uuidRegex: Regex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f‌​]{4}-[0-9a-f]{12}$".r

  def fromString(possibleGameId: String): Validation[GameId] =
    if (isValid(possibleGameId)) Valid(GameId(UUID.fromString(possibleGameId)))
    else Invalid(InvalidGameId(possibleGameId)).toValidatedNel

  private def isValid(possibleGameId: String): Boolean = uuidRegex.findFirstMatchIn(possibleGameId).isDefined
}

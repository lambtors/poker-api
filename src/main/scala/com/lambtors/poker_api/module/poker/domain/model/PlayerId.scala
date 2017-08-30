package com.lambtors.poker_api.module.poker.domain.model

import java.util.UUID

import scala.util.matching.Regex

import cats.data.Validated.{Invalid, Valid}
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final case class PlayerId(playerId: UUID)

object PlayerId {
  private val uuidRegex: Regex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f‌​]{4}-[0-9a-f]{12}$".r

  def fromString(possiblePlayerId: String): Validation[PlayerId] =
    if (isValid(possiblePlayerId)) Valid(PlayerId(UUID.fromString(possiblePlayerId)))
    else Invalid(InvalidPlayerId(possiblePlayerId)).toValidatedNel

  private def isValid(possiblePlayerId: String): Boolean = uuidRegex.findFirstMatchIn(possiblePlayerId).isDefined
}

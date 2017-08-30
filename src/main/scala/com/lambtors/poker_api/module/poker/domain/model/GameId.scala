package com.lambtors.poker_api.module.poker.domain.model

import java.util.UUID

import com.lambtors.poker_api.module.poker.domain.error.InvalidGameId
import com.lambtors.poker_api.module.poker.domain.model.validator.UuidValidator

import cats.data.Validated.{Invalid, Valid}
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final case class GameId(gameId: UUID)

object GameId {
  def fromString(possibleGameId: String): Validation[GameId] =
    if (isValid(possibleGameId)) Valid(GameId(UUID.fromString(possibleGameId)))
    else Invalid(InvalidGameId(possibleGameId)).toValidatedNel

  private def isValid(possibleGameId: String): Boolean = UuidValidator.isValid(possibleGameId)
}

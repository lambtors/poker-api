package com.lambtors.poker_api.module.poker.domain.model

import cats.data.Validated.{Invalid, Valid}
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

import com.lambtors.poker_api.module.poker.domain.error.InvalidAmountOfPlayers

import com.lambtors.poker_api.module.poker.domain.error.InvalidAmountOfPlayers

final case class AmountOfPlayers(amount: Int)

object AmountOfPlayers {
  def fromString(possibleAmountOfPlayers: Int): Validation[AmountOfPlayers] =
    if (isValid(possibleAmountOfPlayers)) Valid(AmountOfPlayers(possibleAmountOfPlayers))
    else Invalid(InvalidAmountOfPlayers(possibleAmountOfPlayers)).toValidatedNel

  private def isValid(possibleAmountOfPlayers: Int): Boolean =
    possibleAmountOfPlayers > 1 && possibleAmountOfPlayers <= 9
}

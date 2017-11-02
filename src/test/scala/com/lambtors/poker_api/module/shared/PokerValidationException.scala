package com.lambtors.poker_api.module.shared

import cats.data.NonEmptyList
import com.lambtors.poker_api.module.poker.domain.error.PokerValidationError

case class PokerValidationException(errors: NonEmptyList[PokerValidationError]) extends Throwable

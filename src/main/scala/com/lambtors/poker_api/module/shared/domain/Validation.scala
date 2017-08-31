package com.lambtors.poker_api.module.shared.domain

import cats.data.ValidatedNel
import com.lambtors.poker_api.module.poker.domain.error.PokerValidationError

object Validation {
  type Validation[T] = ValidatedNel[PokerValidationError, T]
}

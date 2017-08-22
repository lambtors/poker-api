package com.lambtors.poker_api.module.poker.domain.model.validator

import scala.util.matching.Regex

object UuidValidator {
  private val uuidRegex: Regex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f‌​]{4}-[0-9a-f]{12}$".r

  def isValid(possibleUuid: String): Boolean = uuidRegex.findFirstMatchIn(possibleUuid).isDefined
}

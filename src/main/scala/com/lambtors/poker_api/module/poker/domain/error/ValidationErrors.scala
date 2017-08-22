package com.lambtors.poker_api.module.poker.domain.error

sealed trait PokerValidationError extends Throwable

case class InvalidGameId(invalidValue: String) extends PokerValidationError

case class InvalidPlayerId(invalidValue: String) extends PokerValidationError

case class InvalidAmountOfPlayers(invalidValue: Int) extends PokerValidationError

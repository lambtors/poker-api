package com.lambtors.poker_api.module.poker.domain.model

sealed trait PokerValidationError extends Throwable

case class InvalidGameId(invalidValue: String) extends PokerValidationError

case class InvalidAmountOfPlayers(invalidValue: Int) extends PokerValidationError

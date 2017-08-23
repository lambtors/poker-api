package com.lambtors.poker_api.module.poker.domain.error

import com.lambtors.poker_api.module.poker.domain.model.GameId

sealed trait PokerError extends Throwable

final case class PokerGameAlreadyExisting(gameId: GameId) extends PokerError

final case class PokerGameNotFound(gameId: GameId) extends PokerError

final case class FlopNotPossibleWhenItIsAlreadyGiven(gameId: GameId) extends PokerError

final case class TurnNotPossibleWhenItIsAlreadyGiven(gameId: GameId) extends PokerError

final case class RiverNotPossibleWhenItIsAlreadyGiven(gameId: GameId) extends PokerError

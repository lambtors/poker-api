package com.lambtors.poker_api.module.poker.domain.error

import com.lambtors.poker_api.module.poker.domain.model.{FakePlayerId, GameId}

sealed trait PokerError extends Throwable

final case class PokerGameAlreadyExisting(gameId: GameId) extends PokerError

final case class PokerGameNotFound(gameId: GameId) extends PokerError

final case class FakePlayerNotFound(fakePlayerId: FakePlayerId) extends PokerError

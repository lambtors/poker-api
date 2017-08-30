package com.lambtors.poker_api.module.poker.infrastructure.stub

import com.lambtors.poker_api.module.poker.domain.model.{Card, GameId, Player, PlayerId}

object PlayerStub {
  def create(
      playerId: PlayerId = PlayerIdStub.random(),
      gameId: GameId = GameIdStub.random(),
      firstCard: Card = CardStub.random(),
      secondCard: Card = CardStub.random()
  ): Player = Player(playerId, gameId, firstCard, secondCard)

  def random(): Player = create()
}

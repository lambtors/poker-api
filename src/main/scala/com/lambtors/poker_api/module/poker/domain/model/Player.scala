package com.lambtors.poker_api.module.poker.domain.model

import java.util.UUID

case class Player(playerId: PlayerId, gameId: GameId, firstCard: Card, secondCard: Card)

object Player {
  def newPlayer(gameId: GameId): Player = Player(PlayerId(UUID.randomUUID()), gameId, Card.randomCard, Card.randomCard)

  def newPlayerWithCards(gameId: GameId, firstCard: Card, secondCard: Card): Player =
    Player(PlayerId(UUID.randomUUID()), gameId, firstCard, secondCard)
}

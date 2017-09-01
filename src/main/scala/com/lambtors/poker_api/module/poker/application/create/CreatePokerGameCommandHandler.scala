package com.lambtors.poker_api.module.poker.application.create

import cats.implicits._
import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler
import com.lambtors.poker_api.module.poker.domain.model.{AmountOfPlayers, GameId}
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final class CreatePokerGameCommandHandler[P[_]](pokerGameCreator: PokerGameCreator[P])
    extends CommandHandler[P, CreatePokerGameCommand] {

  override def handle(command: CreatePokerGameCommand): Validation[P[Unit]] =
    (AmountOfPlayers.fromString(command.amountOfPlayers), GameId.fromString(command.gameId))
      .mapN(pokerGameCreator.create)
}

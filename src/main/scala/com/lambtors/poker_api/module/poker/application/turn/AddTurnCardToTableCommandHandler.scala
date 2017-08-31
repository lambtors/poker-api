package com.lambtors.poker_api.module.poker.application.turn

import cats.Functor
import cats.implicits._
import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler
import com.lambtors.poker_api.module.poker.domain.model.GameId
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

class AddTurnCardToTableCommandHandler[P[_]: Functor](adder: TurnCardAdder[P])
    extends CommandHandler[P, AddTurnCardToTableCommand] {
  override def handle(command: AddTurnCardToTableCommand): Validation[P[Unit]] =
    GameId.fromString(command.gameId).map(adder.add)
}

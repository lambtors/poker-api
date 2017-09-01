package com.lambtors.poker_api.module.poker.application.turn

import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler
import com.lambtors.poker_api.module.poker.domain.model.GameId
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

class AddTurnCardToTableCommandHandler[P[_]](adder: TurnCardAdder[P])
    extends CommandHandler[P, AddTurnCardToTableCommand] {
  override def handle(command: AddTurnCardToTableCommand): Validation[P[Unit]] =
    GameId.fromString(command.gameId).map(adder.add)
}

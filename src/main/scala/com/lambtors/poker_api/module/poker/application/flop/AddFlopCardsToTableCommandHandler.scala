package com.lambtors.poker_api.module.poker.application.flop

import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler
import com.lambtors.poker_api.module.poker.domain.model.GameId
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final class AddFlopCardsToTableCommandHandler[P[_]](adder: FlopCardsAdder[P])
    extends CommandHandler[P, AddFlopCardsToTableCommand] {
  def handle(command: AddFlopCardsToTableCommand): Validation[P[Unit]] =
    GameId.fromString(command.gameId).map(adder.add)
}

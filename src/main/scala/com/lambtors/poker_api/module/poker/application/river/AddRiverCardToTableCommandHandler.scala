package com.lambtors.poker_api.module.poker.application.river

import cats.Functor
import cats.implicits._
import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler
import com.lambtors.poker_api.module.poker.domain.model.GameId
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

class AddRiverCardToTableCommandHandler[P[_]: Functor](adder: RiverCardAdder[P])
    extends CommandHandler[P, AddRiverCardToTableCommand] {
  override def handle(command: AddRiverCardToTableCommand): Validation[P[Unit]] =
    GameId.fromString(command.gameId).map(adder.add)
}

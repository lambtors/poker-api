package com.lambtors.poker_api.module.poker.application.turn

import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler
import com.lambtors.poker_api.module.poker.domain.model.GameId
import scala.concurrent.Future

import com.lambtors.poker_api.module.shared.domain.Validation.Validation

class AddTurnCardToTableCommandHandler(adder: TurnCardAdder) extends CommandHandler[AddTurnCardToTableCommand] {
  override def handle(command: AddTurnCardToTableCommand): Validation[Future[Unit]] =
    GameId.fromString(command.gameId).map(adder.add)
}

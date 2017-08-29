package com.lambtors.poker_api.module.poker.application.river

import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler
import com.lambtors.poker_api.module.poker.domain.model.GameId
import scala.concurrent.Future

import com.lambtors.poker_api.module.shared.domain.Validation.Validation

class AddRiverCardToTableCommandHandler(adder: RiverCardAdder) extends CommandHandler[AddRiverCardToTableCommand]{
  override def handle(command: AddRiverCardToTableCommand): Validation[Future[Unit]] =
    GameId.fromString(command.gameId).map(adder.add)
}

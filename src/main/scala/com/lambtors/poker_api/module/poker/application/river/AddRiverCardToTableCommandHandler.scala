package com.lambtors.poker_api.module.poker.application.river

import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler
import com.lambtors.poker_api.module.poker.domain.model.GameId

import scala.concurrent.{ExecutionContext, Future}

class AddRiverCardToTableCommandHandler(adder: RiverCardAdder)(implicit ec: ExecutionContext)
  extends CommandHandler[AddRiverCardToTableCommand]{
  override def handle(command: AddRiverCardToTableCommand): Future[Unit] = validate(command).flatMap(adder.add)

  private def validate(command: AddRiverCardToTableCommand) = GameId.fromString(command.gameId)
}

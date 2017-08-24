package com.lambtors.poker_api.module.poker.application.turn

import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler
import com.lambtors.poker_api.module.poker.domain.model.GameId

import scala.concurrent.{ExecutionContext, Future}

class AddTurnCardToTableCommandHandler(adder: TurnCardAdder)(implicit ec: ExecutionContext)
  extends CommandHandler[AddTurnCardToTableCommand] {
  override def handle(command: AddTurnCardToTableCommand): Future[Unit] = validate(command).flatMap(adder.add)

  private def validate(command: AddTurnCardToTableCommand) = GameId.fromString(command.gameId)
}

package com.lambtors.poker_api.module.poker.application.flop

import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler

import scala.concurrent.{ExecutionContext, Future}
import com.lambtors.poker_api.module.poker.domain.model.GameId

final class AddFlopCardsToTableCommandHandler(adder: FlopCardsAdder)(implicit ec: ExecutionContext)
  extends CommandHandler[AddFlopCardsToTableCommand] {
  def handle(command: AddFlopCardsToTableCommand): Future[Unit] = validate(command).flatMap(adder.add)

  private def validate(command: AddFlopCardsToTableCommand) = GameId.fromString(command.gameId)
}

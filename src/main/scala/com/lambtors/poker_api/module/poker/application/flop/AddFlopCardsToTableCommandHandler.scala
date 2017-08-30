package com.lambtors.poker_api.module.poker.application.flop

import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler
import scala.concurrent.Future

import com.lambtors.poker_api.module.poker.domain.model.GameId
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final class AddFlopCardsToTableCommandHandler(adder: FlopCardsAdder)
  extends CommandHandler[AddFlopCardsToTableCommand] {
  def handle(command: AddFlopCardsToTableCommand): Validation[Future[Unit]] =
    GameId.fromString(command.gameId).map(adder.add)
}

package com.lambtors.poker_api.module.poker.application.flop

import scala.concurrent.Future

import cats.Functor
import cats.implicits._
import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler
import com.lambtors.poker_api.module.poker.domain.model.GameId
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final class AddFlopCardsToTableCommandHandler[P[_]: Functor](adder: FlopCardsAdder[P])
    extends CommandHandler[P, AddFlopCardsToTableCommand] {
  def handle(command: AddFlopCardsToTableCommand): Validation[P[Unit]] =
    GameId.fromString(command.gameId).map(adder.add)
}

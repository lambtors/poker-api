package com.lambtors.poker_api.module.poker.application.create

import scala.concurrent.Future

import cats.implicits._
import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler
import com.lambtors.poker_api.module.poker.domain.model.{AmountOfPlayers, GameId}
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

final class CreatePokerGameCommandHandler(pokerGameCreator: PokerGameCreator)
    extends CommandHandler[CreatePokerGameCommand] {

  override def handle(command: CreatePokerGameCommand): Validation[Future[Unit]] =
    (AmountOfPlayers.fromString(command.amountOfPlayers), GameId.fromString(command.gameId))
      .mapN(pokerGameCreator.create)
}

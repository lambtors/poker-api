package com.lambtors.poker_api.module.poker.application.create

import scala.concurrent.{ExecutionContext, Future}

import com.lambtors.poker_api.infrastructure.command_bus.CommandHandler
import com.lambtors.poker_api.module.poker.domain.model.{AmountOfPlayers, GameId}

final class CreatePokerGameCommandHandler(pokerGameCreator: PokerGameCreator)(implicit ec: ExecutionContext)
    extends CommandHandler[CreatePokerGameCommand] {

  override def handle(command: CreatePokerGameCommand): Future[Unit] =
    validate(command).flatMap { validated =>
      val (amountOfPlayers, gameId) = validated
      pokerGameCreator.create(amountOfPlayers, gameId)
    }

  private def validate(command: CreatePokerGameCommand): Future[(AmountOfPlayers, GameId)] =
    for {
      amountOfPlayers <- AmountOfPlayers.fromString(command.amountOfPlayers)
      gameId          <- GameId.fromString(command.gameId)
    } yield (amountOfPlayers, gameId)
}

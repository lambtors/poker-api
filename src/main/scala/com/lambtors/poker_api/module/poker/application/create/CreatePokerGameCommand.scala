package com.lambtors.poker_api.module.poker.application.create

import com.lambtors.poker_api.infrastructure.command_bus.Command

final case class CreatePokerGameCommand(amountOfPlayers: Int, gameId: String) extends Command

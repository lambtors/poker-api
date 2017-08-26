package com.lambtors.poker_api.module.poker.application.flop

import com.lambtors.poker_api.infrastructure.command_bus.Command

case class AddFlopCardsToTableCommand(gameId: String) extends Command

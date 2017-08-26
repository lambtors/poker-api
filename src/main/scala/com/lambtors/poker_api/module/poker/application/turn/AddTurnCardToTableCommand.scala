package com.lambtors.poker_api.module.poker.application.turn

import com.lambtors.poker_api.infrastructure.command_bus.Command

case class AddTurnCardToTableCommand(gameId: String) extends Command

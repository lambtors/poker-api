package com.lambtors.poker_api.module.poker.application.river

import com.lambtors.poker_api.infrastructure.command_bus.Command

case class AddRiverCardToTableCommand(gameId: String) extends Command

package com.lambtors.poker_api.infrastructure.command_bus

import com.lambtors.poker_api.module.shared.domain.Validation.Validation

trait CommandHandler[P[_], C <: Command] {
  def handle(command: C): Validation[P[Unit]]
}

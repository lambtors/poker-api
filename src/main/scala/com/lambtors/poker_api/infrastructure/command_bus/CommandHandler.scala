package com.lambtors.poker_api.infrastructure.command_bus

import scala.concurrent.Future

import com.lambtors.poker_api.module.shared.domain.Validation.Validation

trait CommandHandler[C <: Command] {
  def handle(command: C): Validation[Future[Unit]]
}

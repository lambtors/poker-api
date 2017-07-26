package com.lambtors.poker_api.infrastructure.command_bus

import scala.concurrent.Future

trait CommandHandler[C] {
  def handle(command: C): Future[Unit]
}

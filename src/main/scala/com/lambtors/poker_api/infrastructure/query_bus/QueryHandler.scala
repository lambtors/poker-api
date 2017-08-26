package com.lambtors.poker_api.infrastructure.query_bus

import scala.concurrent.Future

trait QueryHandler[Q <: Query, R] {
  def handle(query: Q): Future[R]
}

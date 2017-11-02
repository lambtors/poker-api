package com.lambtors.poker_api.infrastructure.query_bus

import scala.concurrent.Future

import com.lambtors.poker_api.module.shared.domain.Validation.Validation

trait QueryHandler[Q <: Query, R] {
  def handle(query: Q): Validation[Future[R]]
}

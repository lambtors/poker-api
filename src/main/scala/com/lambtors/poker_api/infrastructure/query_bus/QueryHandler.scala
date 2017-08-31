package com.lambtors.poker_api.infrastructure.query_bus

import com.lambtors.poker_api.module.shared.domain.Validation.Validation

trait QueryHandler[P[_], Q <: Query, R] {
  def handle(query: Q): Validation[P[R]]
}

package com.lambtors.poker_api.module.shared.domain

import java.util.UUID

trait UUIDProvider[P[_]] {
  def provide(): P[UUID]
}

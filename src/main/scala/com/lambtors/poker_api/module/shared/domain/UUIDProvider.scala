package com.lambtors.poker_api.module.shared.domain

import java.util.UUID

trait UUIDProvider {
  def provide(): UUID
}

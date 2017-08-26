package com.lambtors.poker_api.module.shared.infrastructure.provider

import java.util.UUID

import com.lambtors.poker_api.module.shared.domain.UUIDProvider

class RandomUUIDProvider extends UUIDProvider {
  override def provide(): UUID = UUID.randomUUID()
}

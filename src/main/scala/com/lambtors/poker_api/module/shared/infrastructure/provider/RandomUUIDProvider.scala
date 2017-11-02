package com.lambtors.poker_api.module.shared.infrastructure.provider

import java.util.UUID

import scala.concurrent.Future

import com.lambtors.poker_api.module.shared.domain.UUIDProvider

class RandomUUIDProvider extends UUIDProvider[Future] {
  override def provide(): Future[UUID] = Future.successful(UUID.randomUUID())
}

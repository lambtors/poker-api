package com.lambtors.poker_api.module.poker.domain.model

import scala.concurrent.Future

final case class AmountOfPlayers(amount: Int)

object AmountOfPlayers {
  def fromString(possibleAmountOfPlayers: Int): Future[AmountOfPlayers] =
    if (isValid(possibleAmountOfPlayers)) Future.successful(AmountOfPlayers(possibleAmountOfPlayers))
    else Future.failed(InvalidAmountOfPlayers(possibleAmountOfPlayers))

  private def isValid(possibleAmountOfPlayers: Int): Boolean =
    possibleAmountOfPlayers > 1 && possibleAmountOfPlayers <= 9
}

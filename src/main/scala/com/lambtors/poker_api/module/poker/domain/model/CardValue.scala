package com.lambtors.poker_api.module.poker.domain.model

import scala.util.Random

import ca.mrvisser.sealerate

sealed abstract class CardValue(value: String)

object CardValue {
  case object Ace   extends CardValue("A")
  case object Two   extends CardValue("2")
  case object Three extends CardValue("3")
  case object Four  extends CardValue("4")
  case object Five  extends CardValue("5")
  case object Six   extends CardValue("6")
  case object Seven extends CardValue("7")
  case object Eight extends CardValue("8")
  case object Nine  extends CardValue("9")
  case object Ten   extends CardValue("10")
  case object Jay   extends CardValue("J")
  case object Cue   extends CardValue("Q")
  case object Kay   extends CardValue("K")

  def all: Set[CardValue]    = sealerate.values[CardValue]

  def randomValue(): CardValue = Random.shuffle(all).head
}

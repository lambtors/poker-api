package com.lambtors.poker_api.module.poker.domain.model

import scala.util.Random

import ca.mrvisser.sealerate

sealed abstract class CardValue(val value: String)

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

object CardValue {
  val values = sealerate.values[CardValue]

  def randomValue: CardValue = Random.shuffle(values).head
}

object CardValueOrdering {

  private val orderedByValueDescending = List(Ace, Kay, Cue, Jay, Ten, Nine, Eight, Seven, Six, Five, Four, Three, Two)

  val highestValueToLowest = new Ordering[CardValue] {
    override def compare(x: CardValue, y: CardValue): Int =
      orderedByValueDescending.indexOf(x) compareTo orderedByValueDescending.indexOf(y)
  }
}

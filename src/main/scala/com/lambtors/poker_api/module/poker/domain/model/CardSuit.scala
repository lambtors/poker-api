package com.lambtors.poker_api.module.poker.domain.model

import scala.util.Random

import ca.mrvisser.sealerate

sealed abstract class CardSuit(val suitName: String)

case object Diamonds extends CardSuit("diamonds")
case object Clubs extends CardSuit("clubs")
case object Hearts extends CardSuit("hearts")
case object Spades extends CardSuit("spades")

object CardSuit {
  val values = sealerate.values[CardSuit]

  def randomSuit: CardSuit = Random.shuffle(values).head
}

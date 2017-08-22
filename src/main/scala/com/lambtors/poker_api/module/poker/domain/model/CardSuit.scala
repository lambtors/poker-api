package com.lambtors.poker_api.module.poker.domain.model

sealed abstract class CardSuit(suitName: String)

case object Diamonds extends CardSuit("diamonds")
case object Clubs extends CardSuit("clubs")
case object Hearts extends CardSuit("hearts")
case object Spades extends CardSuit("spades")

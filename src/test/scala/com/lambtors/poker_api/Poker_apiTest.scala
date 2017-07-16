package com.lambtors.poker_api

import org.scalatest._
import org.scalatest.Matchers._

final class Poker_apiTest extends WordSpec with GivenWhenThen {
  "Poker_api" should {
    "greet" in {
      Given("a Poker_api")

      val poker_api = new Poker_api

      When("we ask him to greet someone")

      val nameToGreet = "CodelyTV"
      val greeting = poker_api.greet(nameToGreet)

      Then("it should say hello to someone")

      greeting shouldBe "Hello " + nameToGreet
    }
  }
}

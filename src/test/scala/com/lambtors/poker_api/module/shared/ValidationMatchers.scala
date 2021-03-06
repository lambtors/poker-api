package com.lambtors.poker_api.module.shared

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.Try

import cats.data.Validated.{Invalid, Valid}
import com.lambtors.poker_api.module.poker.domain.error.PokerValidationError
import com.lambtors.poker_api.module.shared.domain.Validation.Validation
import org.scalatest.Matchers
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.{Matcher, MatchResult}

trait ValidationMatchers extends Matchers with ScalaFutures {

  override implicit val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = 5.seconds, interval = 30.milliseconds)

  def beValid: Matcher[Validation[_]] = Matcher { (validated: Validation[_]) =>
    MatchResult(
      validated.isValid,
      "{0} was invalid",
      "{0} was valid",
      Vector(validated),
      Vector(validated)
    )
  }

  def beInvalid: Matcher[Validation[_]] = not(beValid)

  def beSuccessfulFuture: Matcher[Validation[Future[_]]] = Matcher {
    case validated @ Invalid(_) => failedResult(validated)
    case validated @ Valid(future) =>
      val futureAttempt = Try(Await.result(future, patienceConfig.timeout))
      MatchResult(
        futureAttempt.isSuccess,
        s"$validated was invalid or the future was NOT successful, ${futureAttempt.failed}",
        s"$validated was valid and the future was successful"
      )
  }

  def beFailedFutureWith(exception: Throwable): Matcher[Validation[Future[_]]] = Matcher {
    case validated @ Invalid(_) => failedResult(validated)
    case validated @ Valid(future) =>
      val futureAttempt = Try(Await.result(future, patienceConfig.timeout))
      MatchResult(
        futureAttempt.failed.toOption.contains(exception),
        s"$validated was successful or not from the expected type",
        s"$validated was successful and of the expected type"
      )
  }

  def haveValidationErrors(expectedErrors: PokerValidationError*): Matcher[Validation[_]] = Matcher {
    case validated @ Valid(_) => failedResult(validated)
    case validated @ Invalid(actualErrors) =>
      def doListsHaveSameElements(first: List[_], second: List[_]): Boolean = {
        first.size == second.size && first.toSet == second.toSet
      }

      MatchResult(
        doListsHaveSameElements(actualErrors.toList, expectedErrors.toList),
        s"$validated didn't include the errors. Expected: $expectedErrors, actual: $actualErrors.",
        s"$validated did include the errors: Expected: $expectedErrors, actual: $actualErrors."
      )
  }

  def beRightContaining[T](value: T): Matcher[Either[_, T]] = Matcher { either =>
    MatchResult(
      either.fold(_ => false, _ === value),
      s"'$either' was not a right matching '$value'.",
      s"'$either' was a right matching '$value', but should not have been."
    )
  }

  def beRightMatchingPredicate[T](predicate: T => Boolean): Matcher[Either[_, T]] = Matcher { either =>
    MatchResult(
      either.fold(_ => false, predicate(_)),
      s"'$either' was not a right matching given predicate.",
      s"'$either' was a right matching given predicate, but should not have been."
    )
  }

  def beLeftContaining[T](value: T): Matcher[Either[T, _]] = Matcher { either =>
    MatchResult(
      either.fold(_ === value, _ => false),
      s"'$either' was not a left matching '$value'.",
      s"'$either' was a left matching '$value', but should not have been."
    )
  }

  private def failedResult[T](context: T) =
    MatchResult(matches = false, s"Shouldn't get here - context: $context", s"Shouldn't get here - context: $context")
}

object ValidationMatchers extends ValidationMatchers

package com.lambtors.poker_api.module.poker.behaviour

import scala.concurrent.Future

import com.lambtors.poker_api.module.poker.domain.PokerGameRepository
import com.lambtors.poker_api.module.poker.domain.model.{GameId, PokerGame}
import org.scalactic.TypeCheckedTripleEquals
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, OneInstancePerTest, WordSpec}
import org.scalatest.concurrent.ScalaFutures

trait PokerBehaviourSpec
    extends WordSpec
    with Matchers
    with TypeCheckedTripleEquals
    with MockFactory
    with OneInstancePerTest
    with ScalaFutures
    with MockedPokerGameRepository

trait MockedPokerGameRepository extends MockFactory {
  protected val pokerGameRepository: PokerGameRepository = mock[PokerGameRepository]

  def shouldFindPokerGame(gameId: GameId, pokerGame: PokerGame): Unit =
    (pokerGameRepository.search _).expects(gameId).once().returning(Future.successful(Some(pokerGame)))

  def shouldNotFindPokerGame(gameId: GameId): Unit =
    (pokerGameRepository.search _).expects(gameId).once().returning(Future.successful(None))

  def shouldInsertPokerGame(pokerGame: PokerGame): Unit =
    (pokerGameRepository.insert _).expects(pokerGame).once().returning(Future.successful(Unit))
}

package com.lambtors.poker_api.module.poker.behaviour

import scala.concurrent.{ExecutionContextExecutor, Future}

import com.lambtors.poker_api.module.poker.application.player_cards.find.{FakePlayer, FakePlayerRepository}
import com.lambtors.poker_api.module.poker.domain.PokerGameRepository
import com.lambtors.poker_api.module.poker.domain.model.{FakePlayerId, GameId, PokerGame}
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
    with MockedFakePlayerRepository {
  implicit val ec: ExecutionContextExecutor = scala.concurrent.ExecutionContext.global
}

trait MockedPokerGameRepository extends MockFactory {
  protected val pokerGameRepository: PokerGameRepository = mock[PokerGameRepository]

  def shouldFindPokerGame(gameId: GameId, pokerGame: PokerGame): Unit =
    (pokerGameRepository.search _).expects(gameId).once().returning(Future.successful(Some(pokerGame)))

  def shouldNotFindPokerGame(gameId: GameId): Unit =
    (pokerGameRepository.search _).expects(gameId).once().returning(Future.successful(None))

  def shouldInsertPokerGame(pokerGame: PokerGame): Unit =
    (pokerGameRepository.insert _).expects(pokerGame).once().returning(Future.successful(Unit))
}

trait MockedFakePlayerRepository extends MockFactory {
  protected val fakePlayerRepository: FakePlayerRepository = mock[FakePlayerRepository]

  def shouldFindFakePlayer(playerId: FakePlayerId, player: FakePlayer): Unit =
    (fakePlayerRepository.find _).expects(playerId).once().returning(Future.successful(Some(player)))

  def shouldNotFindFakePlayer(playerId: FakePlayerId): Unit =
    (fakePlayerRepository.find _).expects(playerId).once().returning(Future.successful(None))
}

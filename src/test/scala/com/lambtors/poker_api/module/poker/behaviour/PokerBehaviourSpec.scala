package com.lambtors.poker_api.module.poker.behaviour

import scala.concurrent.{ExecutionContext, Future}

import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.model.{GameId, Player, PlayerId, PokerGame}
import com.lambtors.poker_api.module.shared.ValidationMatchers
import org.scalactic.TypeCheckedTripleEquals
import org.scalamock.scalatest.MockFactory
import org.scalatest.{OneInstancePerTest, WordSpec}

trait PokerBehaviourSpec
    extends WordSpec
    with TypeCheckedTripleEquals
    with MockFactory
    with OneInstancePerTest
    with ValidationMatchers {
  implicit val ec = ExecutionContext.global

  protected val pokerGameRepository: PokerGameRepository = mock[PokerGameRepository]
  protected val playerRepository: PlayerRepository       = mock[PlayerRepository]

  def shouldFindPokerGame(gameId: GameId, pokerGame: PokerGame): Unit =
    (pokerGameRepository.search _).expects(gameId).once().returning(Future.successful(Some(pokerGame)))

  def shouldNotFindPokerGame(gameId: GameId): Unit =
    (pokerGameRepository.search _).expects(gameId).once().returning(Future.successful(None))

  def shouldInsertPokerGame(pokerGame: PokerGame): Unit =
    (pokerGameRepository.insert _).expects(pokerGame).once().returning(Future.successful(Unit))

  def shouldUpdatePokerGame(pokerGame: PokerGame): Unit =
    (pokerGameRepository.update _).expects(pokerGame).once().returns(Future.successful(Unit))

  def shouldFindPlayersByGameId(gameId: GameId, players: List[Player]): Unit =
    (playerRepository.search(_: GameId)).expects(gameId).once().returns(Future.successful(players))

  def shouldNotFindPlayersByGameId(gameId: GameId): Unit =
    (playerRepository.search(_: GameId)).expects(gameId).once().returns(Future.successful(List.empty))

  def shouldFindPlayer(playerId: PlayerId, player: Player): Unit =
    (playerRepository.search(_: PlayerId)).expects(playerId).once().returns(Future.successful(Some(player)))

  def shouldNotFindPlayer(playerId: PlayerId): Unit =
    (playerRepository.search(_: PlayerId)).expects(playerId).once().returns(Future.successful(None))

  def shouldInsertPlayer(player: Player): Unit =
    (playerRepository.insert _).expects(player).returns(Future.successful(Unit))
}

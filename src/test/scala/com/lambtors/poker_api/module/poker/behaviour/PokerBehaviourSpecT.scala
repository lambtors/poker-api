package com.lambtors.poker_api.module.poker.behaviour

import cats.Functor
import cats.implicits._
import cats.data.{OptionT, StateT}
import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.model.{GameId, Player, PlayerId, PokerGame}
import com.lambtors.poker_api.module.shared.ValidationMatchers
import com.lambtors.poker_api.module.shared.domain.Validation.Validation
import org.scalatest.{Matchers, WordSpec}

trait PokerStateT {
  case class PokerState(pokerGameRepositoryState: Map[GameId, PokerGame],
                        playerRepositoryState: Map[GameId, Map[PlayerId, Player]])

  object PokerState {
    val empty = PokerState(Map.empty, Map.empty)
  }

  type R[A] = Either[Throwable, A]
  type Q[A] = StateT[R, PokerState, A]

  object Q {
    def apply[A](f: PokerState => R[(PokerState, A)]): StateT[R, PokerState, A] = StateT[R, PokerState, A](f)
  }

  type P[A] = Validation[Q[A]]

  val pokerGameRepository: PokerGameRepository[Q] = new PokerGameRepository[Q] {
    override def insert(pokerGame: PokerGame): Q[Unit] = Q[Unit] { state =>
      Right(state.copy(pokerGameRepositoryState = state.pokerGameRepositoryState + (pokerGame.gameId -> pokerGame)), ())
    }

    override def update(pokerGame: PokerGame): Q[Unit] = Q[Unit] { state =>
      Right(state.copy(pokerGameRepositoryState = state.pokerGameRepositoryState + (pokerGame.gameId -> pokerGame)), ())
    }

    override def search(gameId: GameId): OptionT[Q, PokerGame] =
      OptionT[Q, PokerGame](Q[Option[PokerGame]] { state =>
        Right((state, state.pokerGameRepositoryState.get(gameId)))
      })
  }

  val playerRepository: PlayerRepository[Q] = new PlayerRepository[Q] {
    override def insert(player: Player): Q[Unit] = Q[Unit] { state =>
      Right(
        state.copy(
          playerRepositoryState = state.playerRepositoryState + (player.gameId                  ->
            (state.playerRepositoryState.getOrElse(player.gameId, Map.empty) + (player.playerId -> player)))),
        ()
      )
    }

    override def search(playerId: PlayerId): OptionT[Q, Player] =
      OptionT[Q, Player](Q[Option[Player]] { state =>
        Right((state, state.playerRepositoryState.values.toList.flatten.toMap.get(playerId)))
      })

    override def search(gameId: GameId): Q[List[Player]] = Q[List[Player]] { state =>
      Right((state, state.playerRepositoryState.get(gameId).fold[List[Player]](Nil)(_.values.toList)))
    }
  }
}

abstract class PokerBehaviourSpecT extends WordSpec with Matchers with PokerStateT with ValidationMatchers

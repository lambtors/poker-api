package com.lambtors.poker_api.module.poker.behaviour

import cats.Functor
import cats.data.{OptionT, StateT}
import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.model.{GameId, Player, PlayerId, PokerGame}
import com.lambtors.poker_api.module.shared.domain.Validation.Validation

abstract class PokerGameRepositoryState {
  type PokerGameRepositoryState = Map[GameId, PokerGame]

  type R[A] = Either[Throwable, A]
  type Q[A] = StateT[R, PokerGameRepositoryState, A]

  object Q {
    def apply[A](
      f: PokerGameRepositoryState => R[(PokerGameRepositoryState, A)]): StateT[R, PokerGameRepositoryState, A] =
      StateT[R, PokerGameRepositoryState, A](f)
  }

  type P[A] = Validation[Q[A]]

  val pokerGameRepository: PokerGameRepository[Q] = new PokerGameRepository[Q] {
    override def insert(pokerGame: PokerGame): Q[Unit] = Q[Unit] { repo =>
      Right((repo + (pokerGame.gameId -> pokerGame), Unit))
    }

    override def update(pokerGame: PokerGame): Q[Unit] = Q[Unit] { repo =>
      Right((repo + (pokerGame.gameId -> pokerGame), Unit))
    }

    override def search(gameId: GameId): OptionT[Q, PokerGame] =
      OptionT[Q, PokerGame](Q[Option[PokerGame]] { repo =>
        Right((repo, repo.get(gameId)))
      })
  }
}

abstract class PlayerRepositoryState {
  type PlayerRepositoryState = Map[GameId, Map[PlayerId, Player]]

  type R[A] = Either[Throwable, A]
  type Q[A] = StateT[R, PlayerRepositoryState, A]

  object Q {
    def apply[A](
      f: PlayerRepositoryState => R[(PlayerRepositoryState, A)]): StateT[R, PlayerRepositoryState, A] =
      StateT[R, PlayerRepositoryState, A](f)
  }

  type P[A] = Validation[Q[A]]

  val playerRepository: PlayerRepository[Q] = new PlayerRepository[Q] {
    override def insert(player: Player): Q[Unit] =  Q[Unit] { repo =>
      Right((repo + (player.gameId -> Map[PlayerId, Player](player.playerId, player)), Unit))
    }

    override def search(playerId: PlayerId): OptionT[Q, Player] = OptionT[Q, Player](Q[Option[Player]] { repo =>
      Right((repo, {
        val bla = repo.map {
          case (gameId, playersMap) =>
      }}))
    })

    override def search(gameId: GameId): Q[List[Player]] = Q[List[Player]]
  }
}

abstract class PokerBehaviourSpecT {
  /* Instances needed for puretest */

  implicit def _1[F[_]: Functor, G[_]: Functor]: Functor[({
    type λ[α] = F[G[α]]
  })#λ] = Functor[F].compose[G]
}

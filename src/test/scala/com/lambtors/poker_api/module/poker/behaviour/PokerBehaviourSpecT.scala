package com.lambtors.poker_api.module.poker.behaviour

import java.util.UUID

import cats.Functor
import cats.implicits._
import cats.data.{OptionT, StateT}
import com.lambtors.poker_api.module.poker.domain.{PlayerRepository, PokerGameRepository}
import com.lambtors.poker_api.module.poker.domain.error.PokerError
import com.lambtors.poker_api.module.poker.domain.model._
import com.lambtors.poker_api.module.shared.ValidationMatchers
import com.lambtors.poker_api.module.shared.domain.{DeckProvider, UUIDProvider}
import org.scalatest.{Matchers, WordSpec}

trait PokerStateT {
  case class PokerState(pokerGameRepositoryState: Map[GameId, PokerGame],
                        playerRepositoryState: Map[GameId, Map[PlayerId, Player]],
                        uuidProvisions: List[UUID],
                        deckProviderState: List[List[Card]]) {
    def withUuid(uuid: UUID): PokerState = copy(uuidProvisions = uuidProvisions :+ uuid)

    def withPlayer(player: Player): PokerState =
      copy(
        playerRepositoryState = playerRepositoryState + (player.gameId                  ->
          (playerRepositoryState.getOrElse(player.gameId, Map.empty) + (player.playerId -> player))))

    def withPlayers(players: List[Player]): PokerState = players match {
      case head :: tail => withPlayer(head).withPlayers(tail)
      case Nil          => this
    }

    def withGame(pokerGame: PokerGame): PokerState =
      copy(pokerGameRepositoryState = pokerGameRepositoryState + (pokerGame.gameId -> pokerGame))

    def withDeck(deck: List[Card]): PokerState =
      copy(deckProviderState = deckProviderState :+ deck)

    def withoutDecks(): PokerState =
      copy(deckProviderState = Nil)
  }

  object PokerState {
    val empty = PokerState(Map.empty, Map.empty, Nil, Nil)
  }

  type R[A] = Either[Throwable, A]
  type Q[A] = StateT[R, PokerState, A]

  object Q {
    def apply[A](f: PokerState => R[(PokerState, A)]): StateT[R, PokerState, A] = StateT[R, PokerState, A](f)
  }

  val pokerGameRepository: PokerGameRepository[Q] = new PokerGameRepository[Q] {
    override def insert(pokerGame: PokerGame): Q[Unit] = Q[Unit] { state =>
      Right(state.withGame(pokerGame), ())
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
      Right(state.withPlayer(player), ())
    }

    override def search(playerId: PlayerId): OptionT[Q, Player] =
      OptionT[Q, Player](Q[Option[Player]] { state =>
        Right((state, state.playerRepositoryState.values.toList.flatten.toMap.get(playerId)))
      })

    override def search(gameId: GameId): Q[List[Player]] = Q[List[Player]] { state =>
      Right((state, state.playerRepositoryState.get(gameId).fold[List[Player]](Nil)(_.values.toList)))
    }
  }

  val uuidProviderStateT: UUIDProvider[Q] = new UUIDProvider[Q] {
    override def provide(): Q[UUID] = Q[UUID] { state =>
      val uuid :: leftoverUuids = state.uuidProvisions
      Right(state.copy(uuidProvisions = leftoverUuids), uuid)
    }
  }

  val deckProviderStateT: DeckProvider[Q] = new DeckProvider[Q] {
    override def provide(): Q[List[Card]] = Q[List[Card]] { state =>
      Right(state.copy(deckProviderState = state.deckProviderState.tail), state.deckProviderState.head)
    }

    override def shuffleGivenDeck(deck: List[Card]): Q[List[Card]] = Q[List[Card]] { state =>
      Right(state.copy(deckProviderState = state.deckProviderState.tail), state.deckProviderState.head)
    }
  }
}

abstract class PokerBehaviourSpecT extends WordSpec with Matchers with PokerStateT with ValidationMatchers

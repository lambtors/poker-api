package com.lambtors.poker_api.module.shared.syntax

object Syntax {
  implicit class CartesianProduct[X](xs: Traversable[X]) {
    def cross[Y](ys: Traversable[Y]): Traversable[(X, Y)] = for { x <- xs; y <- ys } yield (x, y)
  }
}

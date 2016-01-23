package scato
package data

import clazz._
import Disjunction.{\/, L_, R_}

trait DisjunctionInstances {
  implicit def monad[L]: Monad[L \/ ?] = new Monad[L \/ ?] {
    override val applicative = new Applicative[L \/ ?] {
      override val apply = new Apply[L \/ ?] {
        override val functor = new Functor[L \/ ?] {
          override def map[A, B](ma: L \/ A)(f: A => B): L \/ B =
            ma.fold[L \/ B](l => L_(l))(r => R_(f(r)))
        }
        override def ap[A, B](ma: L \/ A)(mf: L \/ (A => B)): L \/ B =
          ma.fold[L \/ B](l => L_(l))(a => functor.map[(A => B), B](mf)(f => f(a)))
      }
      override def pure[A](a: A): L \/ A =
        R_[A](a)
    }

    override val bind = new Bind[L \/ ?] {
      def apply = applicative.apply
      override def flatMap[A, B](oa: L \/ A)(f: A => L \/ B): L \/ B =
        oa.fold[L \/ B](l => L_(l))(a => f(a))
    }
  }

  implicit def applicative[L]: Applicative[L \/ ?] = monad[L].applicative
  implicit def apply[L]: Apply[L \/ ?] = monad[L].applicative.apply
  implicit def functor[L]: Functor[L \/ ?] = monad[L].applicative.apply.functor
  implicit def bind[L]: Bind[L \/ ?] = monad[L].bind
}

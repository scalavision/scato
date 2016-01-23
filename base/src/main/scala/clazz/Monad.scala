package scato
package clazz

abstract class Monad[M[_]] {
  def applicative: Applicative[M]
  def bind: Bind[M]
}

object Monad extends MonadInstances with MonadInstancesStdLib {
  def apply[M[_]](implicit M: Monad[M]): Monad[M] = M
}


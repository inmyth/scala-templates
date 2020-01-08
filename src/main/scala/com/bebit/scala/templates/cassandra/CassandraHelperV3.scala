package com.bebit.scala.templates.cassandra

import com.datastax.driver.core.{PreparedStatement, ResultSet, ResultSetFuture, Session, SimpleStatement}
import com.google.common.util.concurrent.{FutureCallback, Futures, ListenableFuture}
import monix.eval.Task

import scala.concurrent.{Future, Promise}

object CassandraHelperV3 {

  implicit class CqlStrings(val context: StringContext) extends AnyVal {

    def cql(args: Any*)(implicit session: Session): Task[PreparedStatement] = {
      val statement = new SimpleStatement(context.raw(args: _*))
      session.prepareAsync(statement)
    }
  }

  def execute(statement: Task[PreparedStatement], params: Any*)(implicit session: Session): Task[ResultSet] =
    statement
      .map(p => {
        if(params.nonEmpty) {
          val x = params.map(_.asInstanceOf[Object])
          p.bind(x: _*)
        }
        else p.bind()
      })
      .flatMap(session.executeAsync(_))

  implicit def resultSetFutureToScala(f: ResultSetFuture): Task[ResultSet] = {
    val p = Promise[ResultSet]()
    Futures.addCallback(f,
      new FutureCallback[ResultSet] {
        def onSuccess(r: ResultSet) = p success r
        def onFailure(t: Throwable) = p failure t
      })
    Task.fromFuture(p.future)
  }

  implicit def toScalaFuture[T](lFuture: ListenableFuture[T]): Task[T] = {
    val p = Promise[T]
    Futures.addCallback(lFuture,
      new FutureCallback[T] {
        def onSuccess(result: T) = p.success(result)
        def onFailure(t: Throwable) = p.failure(t)
      })
    Task.fromFuture(p.future)
  }



}

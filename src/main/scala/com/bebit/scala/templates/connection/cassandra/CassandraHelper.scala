package com.bebit.scala.templates.connection.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.{AsyncResultSet, PreparedStatement, SimpleStatement}
import monix.eval.Task


object CassandraHelper {

  implicit class CqlStrings(val context: StringContext) extends AnyVal {

    def cql(args: Any*)(implicit session: CqlSession): Task[PreparedStatement] = {
      val statement =  SimpleStatement.newInstance(context.raw(args: _*))
      Task.fromFuture(scala.compat.java8.FutureConverters.toScala(session.prepareAsync(statement)))
    }
  }

  def execute(statement: Task[PreparedStatement], params: Any*)( implicit session: CqlSession): Task[AsyncResultSet] =
    statement
      .map(p => if(params.nonEmpty) p.bind(params.map(_.asInstanceOf[Object]) ) else p.bind())
      .flatMap(p => Task.fromFuture(scala.compat.java8.FutureConverters.toScala(session.executeAsync(p)))
    )
}
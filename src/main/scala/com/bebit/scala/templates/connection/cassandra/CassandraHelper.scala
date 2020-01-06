package com.bebit.scala.templates.connection.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.{AsyncResultSet, PreparedStatement, SimpleStatement}

import scala.concurrent.{ExecutionContext, Future}

object CassandraHelper {

  implicit class CqlStrings(val context: StringContext) extends AnyVal {

    def cql(args: Any*)(implicit session: CqlSession): Future[PreparedStatement] = {
      val statement =  SimpleStatement.newInstance(context.raw(args: _*))
      scala.compat.java8.FutureConverters.toScala(session.prepareAsync(statement))
    }
  }

  def execute(statement: Future[PreparedStatement], params: Any*)(implicit executionContext: ExecutionContext, session: CqlSession): Future[AsyncResultSet] =
    statement
      .map(p => if(params.nonEmpty) p.bind(params.map(_.asInstanceOf[Object]) ) else p.bind())
      .map(session.executeAsync(_))
      .flatMap(scala.compat.java8.FutureConverters.toScala)
}
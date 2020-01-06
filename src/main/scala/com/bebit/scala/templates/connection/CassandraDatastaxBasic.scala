package com.bebit.scala.templates.connection

import com.bebit.scala.templates.connection.cassandra.CassandraHelper
import com.datastax.oss.driver.api.core.CqlSession

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object CassandraDatastaxBasic extends App {
  import CassandraHelper._

  import monix.execution.Scheduler.Implicits.global

  implicit val session = CqlSession
    .builder()
    .build()

  val compose = execute(cql"select release_version from system.local").runToFuture

  compose
    .map(p => {
      val result = p.one().getString("release_version")
      println(result)
    })
    .recover{case e => println(e)}

  Await.ready(compose, Duration.Inf)
}



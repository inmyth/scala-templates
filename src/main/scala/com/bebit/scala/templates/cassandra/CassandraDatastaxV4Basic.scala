package com.bebit.scala.templates.cassandra

import com.datastax.oss.driver.api.core.CqlSession

object CassandraDatastaxV4Basic extends App {
 import CassandraHelperV4._
  import monix.execution.Scheduler.Implicits.global

  implicit val session = CqlSession
    .builder()
    .build()

  sys.addShutdownHook(session.close())

  val compose = execute(cql"select release_version from system.local")

  compose.runToFuture
    .map(p => {
      val result = p.one().getString("release_version")
      println(result)
    })
    .recover{case e => println(e)}

}

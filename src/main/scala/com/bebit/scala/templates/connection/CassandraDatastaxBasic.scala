package com.bebit.scala.templates.connection

import com.bebit.scala.templates.connection.cassandra.CassandraHelper
import com.datastax.oss.driver.api.core.CqlSession

object CassandraDatastaxBasic extends App {
  import CassandraHelper._

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val session = CqlSession
    .builder()
    .build()

  execute(cql"select release_version from system.local")
    .map(p => {
      val result = p.one().getString("release_version")
      println(result)
    })
    .recover{case e => println(e)}

}



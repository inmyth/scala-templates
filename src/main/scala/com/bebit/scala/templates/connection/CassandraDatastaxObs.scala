package com.bebit.scala.templates.connection

import java.math.BigInteger

import com.bebit.scala.templates.connection.CassandraDatastaxBasic.session
import com.bebit.scala.templates.connection.cassandra.CassandraHelper.execute
import com.datastax.oss.driver.api.core.{CqlIdentifier, CqlSession}
import com.datastax.oss.driver.api.core.config.{DefaultDriverOption, DriverConfigLoader}

import scala.jdk.CollectionConverters._
/*
  CREATE KEYSPACE IF NOT EXISTS my_keyspace
  WITH replication = {
    'class':'SimpleStrategy',
    'replication_factor' : 3
  };
 */
object CassandraDatastaxObs extends App {
  import cassandra.CassandraHelper._

  import monix.execution.Scheduler.Implicits.global

  implicit val session = CqlSession.builder()
    .withConfigLoader(DriverConfigLoader.fromClasspath("cassandra-samples/application.conf"))
    .build();

  sys.addShutdownHook(session.close())

  val compose = for {
    _ <- execute(cql"DROP TABLE emp")
    _ <- execute(
      cql"""
        CREATE TABLE emp(
         id int PRIMARY KEY,
         name TEXT,
         phone VARINT
        );
         """)
    _ <- execute(cql"INSERT INTO emp(id, name, phone) VALUES(?, ?, ?);", 1, "martin", BigInteger.valueOf(3453453453L))
    t <- execute(cql"SELECT * FROM emp WHERE id = ?;", 1)
  } yield t

  compose.runToFuture
    .map(p => println (p.one().getString(CqlIdentifier.fromCql("name"))))
    .recover{case e => println(e)}
}

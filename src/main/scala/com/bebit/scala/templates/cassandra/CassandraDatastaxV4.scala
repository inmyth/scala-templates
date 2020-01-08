package com.bebit.scala.templates.cassandra

import java.math.BigInteger

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.config.DriverConfigLoader
import com.github.tototoshi.csv.CSVReader
import monix.eval.Task
import monix.reactive.Observable

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/*
  This setup works on local with the keyspace:
  CREATE KEYSPACE IF NOT EXISTS my_keyspace
  WITH replication = {
    'class':'SimpleStrategy',
    'replication_factor' : 3
  };

  And in config
  request.consistency = ONE

 */
object CassandraDatastaxV4 extends App {
  import CassandraHelperV4._
  import monix.execution.Scheduler.Implicits.global

  val fileName = "/csv-samples/cli_t-30_generated.csv"
  implicit val session = CqlSession.builder()
    .withConfigLoader(DriverConfigLoader.fromClasspath("cassandra-samples/application.conf"))
    .build();
  sys.addShutdownHook(session.close())

  val compose = for {
    _ <- execute(cql"DROP TABLE emp")
    _ <- execute(
      cql"""
        CREATE TABLE emp(
         id VARINT PRIMARY KEY,
         field1 TEXT,
         field2 VARINT,
         field4 VARINT
        );
         """)
  } yield ()
  val x = compose.runToFuture
  Await.ready(x, Duration.Inf)

  def chainInsertThenSelect(csvrow: List[String]) = for {
    _ <- execute(cql"INSERT INTO emp(id, field1, field2, field4) VALUES(?, ?, ?, ?);", new BigInteger(csvrow(2)), csvrow.head, new BigInteger(csvrow(1)), new BigInteger(csvrow(3)))
    rs <- execute(cql"SELECT field1 FROM emp WHERE id = ?", new BigInteger(csvrow(2)))
    id <- Task (rs.one().getString(0))
  } yield id

  val reader = CSVReader.open(getClass.getResource(fileName).getFile)
  val stream = Observable.fromIterable(reader.toStream)
    .dump("start")
    .mapEval(p => (if(p.head == "a") Task.raiseError(new Exception("this is a header")) else Task.now(p)).attempt)
    .collect{ case Right(v) => v}
    .mapEval(chainInsertThenSelect)
    .foreachL(println)
    .runToFuture

  Await.ready(stream, Duration.Inf)

}

package com.bebit.scala.templates.cassandra

import java.io.File
import java.math.BigInteger
import java.net.InetAddress

import com.datastax.driver.core.Cluster
import com.datastax.driver.mapping.MappingManager
import com.github.tototoshi.csv.CSVReader
import com.typesafe.config.ConfigFactory
import monix.eval.Task
import monix.reactive.{Observable, OverflowStrategy}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.jdk.CollectionConverters._

object CassandraDatastaxV3 extends App{
  import CassandraHelperV3._
  import monix.execution.Scheduler.Implicits.global

  val fileName = "/csv-samples/cli_t-30_generated.csv"
  val casConfig = ConfigFactory.parseResources("./cassandra-samples/application.conf")
  val contactPoints = casConfig.getStringList("datastax-java-driver.basic.contact-points").asScala.map(p => InetAddress.getByName(p.split(":")(0))).asJava
  val keyspace =  casConfig.getString("datastax-java-driver.basic.session-keyspace")

  val cluster = Cluster.builder().addContactPoints(contactPoints).withoutMetrics().build()
  implicit val session = cluster.connect(keyspace)
  val manager = new MappingManager(session)

  sys.addShutdownHook(session.close())

  val compose = for {
    _ <- execute(cql"DROP TABLE dummy")
    _ <- execute(
      cql"""
        CREATE TABLE dummy (
           service_id TEXT,
           data_id TEXT,
           acquisition_day TEXT,
           price TEXT,
           comment TEXT,
           PRIMARY KEY (service_id, data_id)
        );
         """)

  } yield ()
  val x = compose.runToFuture
  Await.ready(x, Duration.Inf)

  def csvRowToDummy(csvRow: List[String]) = new Dummy(csvRow.head, csvRow(1), csvRow(2), csvRow(3), csvRow(4))

  def chainInsertThenSelect(csvrow: List[String]) = for {
    acs <- Task{ manager.createAccessor(classOf[QueriesV3]) }
    mgr <- Task{ manager.mapper(classOf[Dummy]) }
    _ <- mgr.saveAsync(csvRowToDummy(csvrow))
    dummy <- mgr.getAsync(csvrow.head, csvrow(1))
//    dummy <- q.fetchDummy(csvrow.head, csvrow(1))
  } yield dummy

  val reader = CSVReader.open(getClass.getResource(fileName).getFile)
  val stream = Observable.fromIterable(reader.toStream)
    .dump("start")
    .asyncBoundary(OverflowStrategy.BackPressure(10))
    .mapEval(p => (if(p.head == "サービスID") Task.raiseError(new Exception("this is header")) else Task.now(p)).attempt)
    .collect{ case Right(v) => v}
    .mapEval(chainInsertThenSelect)
    .foreachL(println)
    .runToFuture
/*
    .mapEval(id => Task(updateTable(id)).map(result => (result, id))
    .mapEval { case (updateResult, id) => ??? }
 */
  Await.ready(stream, Duration.Inf)


}

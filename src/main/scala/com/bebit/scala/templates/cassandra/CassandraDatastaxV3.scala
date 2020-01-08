package com.bebit.scala.templates.cassandra

import java.io.File
import java.math.BigInteger
import java.net.InetAddress

import com.datastax.driver.core.Cluster
import com.datastax.driver.mapping.MappingManager
import com.github.tototoshi.csv.CSVReader
import com.typesafe.config.ConfigFactory
import monix.eval.Task
import monix.reactive.Observable

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

//  manager.mapper(classOf[Dummy]).get()

  sys.addShutdownHook(session.close())
  val queries = manager.createAccessor(classOf[QueriesV3])


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


//
////  val manager = new MappingManager(session)
////  val mapper = manager.mapper(classOf[Nothing])
//
//  def chainInsertThenSelect(csvrow: List[String]) = for {
//
//
//
//    _ <- execute(cql"INSERT INTO emp(id, field1, field2, field4) VALUES(?, ?, ?, ?);", new BigInteger(csvrow(2)), csvrow.head, new BigInteger(csvrow(1)), new BigInteger(csvrow(3)))
//    rs <- execute(cql"SELECT field1 FROM emp WHERE id = ?", new BigInteger(csvrow(2)))
//    id <- Task (rs.one().getString(0))
//  } yield id
//
//  val reader = CSVReader.open(getClass.getResource(fileName).getFile)
//  val stream = Observable.fromIterable(reader.toStream)
//    .dump("start")
//    .mapEval(p => (if(p.head == "a") Task.raiseError(new Exception("this is a header")) else Task.now(p)).attempt)
//    .collect{ case Right(v) => v}
//    .mapEval(chainInsertThenSelect)
//    .foreachL(println)
//    .runToFuture
//
//  Await.ready(stream, Duration.Inf)


}

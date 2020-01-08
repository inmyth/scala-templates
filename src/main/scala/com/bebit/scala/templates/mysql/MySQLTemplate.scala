package com.bebit.scala.templates.mysql

import java.util.concurrent.TimeUnit

import com.github.jasync.sql.db.general.ArrayRowData
import com.github.jasync.sql.db.{Configuration, ConnectionPoolConfiguration, SSLConfiguration}
import com.github.jasync.sql.db.mysql.pool.MySQLConnectionFactory
import com.github.jasync.sql.db.mysql.{MySQLConnection, MySQLConnectionBuilder}
import com.github.jasync.sql.db.pool.{ConnectionPool, PoolConfiguration}
import monix.eval.Task

import scala.compat.java8.FutureConverters
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}


object MySQLTemplate extends App {

  import monix.execution.Scheduler.Implicits.global

  val host = "localhost";
  val port = "3307";
  val database = "usergram_webapps_client";
  val username = "root" /*"root"*/ ;
  val password = "password" /*password"*/ ;
  val testClientCodename = "clientA";

  val endpoint = s"jdbc:mysql://$host:$port/$database?user=$username&password=$password"
  val connection = MySQLConnectionBuilder.createConnectionPool(endpoint).connect().get()

  val sql = "SELECT id FROM client WHERE codename = ?"
  val f = for {
    f1 <- Task.fromFuture {
      FutureConverters.toScala(connection.sendPreparedStatement(sql, java.util.Arrays.asList(testClientCodename)))
    }
    f2 <- Task {
      f1.getRows.get(0).asInstanceOf[ArrayRowData].get(0).asInstanceOf[Short]
    }

  } yield f2
  f.runToFuture.map(println) recover { case e => println(e) }

  Thread.sleep(2000)

}


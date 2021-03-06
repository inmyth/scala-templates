package com.bebit.scala.templates.observable

import java.io.File
import java.nio.file.Path

import com.github.tototoshi.csv.CSVReader
import com.typesafe.config.ConfigFactory
import monix.eval.Task

import scala.concurrent.Await
import monix.execution.Scheduler.Implicits.global
import monix.reactive._

import concurrent.duration.{Duration, _}
import scala.io.Source
import scala.language.postfixOps
import scala.reflect.io
import scala.reflect.io.Path

/*
  Template for Validator project
  - Read from files
  - Validate input (missing fields, etc)
  - If OK, trigger importer.
  - If ERROR, dump the error log.
 */
object ObsValidator extends App{

    val fileName = "/csv-samples/1575021601-cli_t-30_template.csv"
    val reader = CSVReader.open(getClass.getResource(fileName).getFile)

    val csvStream = Observable.fromIterable(reader.toStream)
      .dump("start")
      .mapEval(p => (if(p.head == "サービスID") Task.raiseError(new Exception("header")) else Task.now(p))
        .attempt)
      .collect{ case Right(v) => v}
        .foreachL(println)
        .runToFuture
    Await.ready(csvStream, Duration.Inf)


}

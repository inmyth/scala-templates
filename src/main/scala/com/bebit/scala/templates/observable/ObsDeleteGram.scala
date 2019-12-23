package com.bebit.scala.templates.observable

import java.io.File
import com.github.tototoshi.csv.CSVReader
import monix.eval.Task
import scala.concurrent.Await
import monix.reactive._
import concurrent.duration.{Duration, _}
import scala.language.postfixOps
object ObsDeleteGram extends App{

  /*
  This Observable is to imitate DeleteGram service.
  It uses backpressure to allow processing in batches.
  DeleteGram performs these steps:
  - read grams from CSV
  - deleteGram
  - deleteHH24
  - fetch user
  - updateUserInfo
  - updateGram


  */

  import monix.execution.Scheduler.Implicits.global



  def simFetch(i : Int, time: Int) = Task {
    Thread.sleep(time)
    println(s"Fetced $i as $i-fetch")

    i
  }

  def simDel(i: Int, time: Int) = Task.gather(
    List(
      Task{Thread.sleep(time); println(s"Delete $i-a done"); None},
      Task{Thread.sleep(time); println(s"Delete $i-b done"); None},
      Task.now(Some(i))
    )
  )

  def simGet(i: Int, time: Int) = Task {
    Thread.sleep(time)
    val res = Vector(i * 10, i * 100, i * 1000)
    println(s"Get after delete ${res} return ")
    res
  }

  def simPars(in: Seq[Int], time: Int) = Task {
    Thread.sleep(time)
    println(s"Parse ${in.head / 10}")
    in.head / 10
  }

  def simUpd(i: Int, time: Int) = Task {
    Thread.sleep(time)
    println(s"Update $i done")
    i
  }

  def compose = {
    val f = Observable.fromIterable(1 to 10)
      .dump("start")
      .asyncBoundary(OverflowStrategy.BackPressure(2))
      .mapEval(p => simFetch(p, 1000)).onErrorRestartUnlimited
      .mapEval(i => simDel(i, 1000))
      .mapEval(p => Task(p.flatten.head))
      .mapEval(p => simGet(p, 1000))
      .mapEval(p => simPars(p, 100))
      .mapEval(p => simUpd(p, 1000))
      .dump("finish")
      .completedL

    //  val consumer: Consumer[String, String] =
    //    Consumer.foldLeft("")(_ + _)
    //  stream.foreach(println)

    Await.ready(f.runToFuture, Duration.Inf)
  }


  val reader = CSVReader.open(new File("sample.csv"))
  val csvStream = Observable.fromIterable(reader.toStream)
    .dump("start")
    .asyncBoundary(OverflowStrategy.BackPressure(2))
    .mapEval(i => Task {

      println(s"Processing $i"); i
    }.delayExecution(20000.millis))
      .completedL


  Await.ready(csvStream.runToFuture, Duration.Inf)







}


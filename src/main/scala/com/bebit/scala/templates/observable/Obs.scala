package com.bebit.scala.templates.observable
import monix.eval.Task

import scala.concurrent.{Await, Future}
import monix.execution.Scheduler.Implicits.global
import monix.reactive._

import concurrent.duration.{Duration, _}
import scala.language.postfixOps

object Obs extends App {

  /*
    Observable evaluates each element and passes it to next step one-by-one.
    Observable needs to be attached to a subscriber to run.
    We can create subscriber ourselves but we can use built-in subscribers like completedL
    (or any methods that end with capital L on Observable)

   */

  val stream =
    Observable("A", "B", "C", "D", "E")
      .dump("start")
      .mapEval(i => Task {println(s"Processing $i"); i ++ i})
      .mapEval(i => Task {println(s"Processing $i"); i}.delayExecution(2 second))
      .dump("finish")
      .completedL

  Await.ready(stream.runToFuture, Duration.Inf)

}

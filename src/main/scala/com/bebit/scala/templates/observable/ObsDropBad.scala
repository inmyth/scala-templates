package com.bebit.scala.templates.observable

import monix.eval.Task
import monix.reactive._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.language.postfixOps

/*
  This composition shows how to drop a bad element
 */

object ObsDropBad extends App{
  import monix.execution.Scheduler.Implicits.global

  val f = Observable.fromIterable(1 to 5)
    .mapEval(p => (if(p%2 == 0) Task.raiseError(new Exception) else Task(p)).attempt)
    .collect { case Right(evt) => evt}
    .mapEval(p => Task{println(p); p})
    .completedL
    .runToFuture

    Await.ready(f, Duration.Inf)


}

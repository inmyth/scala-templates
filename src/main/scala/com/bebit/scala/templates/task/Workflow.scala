package com.bebit.scala.templates.task

import monix.eval.Task

import scala.util.{Failure, Success}

/*
  A program to demonstrate a simple workflow.
  In Scala we can compose executions from start to finish with Future or Task (Monix).
 */
object Workflow extends App{

  case class DeleteKey(userId: String, timeUsec: Long, visitId: String, seqNo: Long)

    import monix.execution.Scheduler.Implicits.global

    lazy val toDeleteKey = (line: List[String]) =>  DeleteKey(line.head, line(1).toLong, line(2), line.last.toLong)

    def parseCSV(in: List[List[String]]) = in.map(toDeleteKey)

    // this is the composition of the program
    val compose1 = for {
      f1 <- Task.now {
        List (
          List("user_id", "123456789", "visit_id", "0"),
          List("user_id", "aaaaa", "visit_id", "0")
        )
      }
      f2 <- Task{ parseCSV(f1) }
      // we add next process here
    } yield f2

    compose1.runToFuture.onComplete {

      case Success(value) => println(value)

      // if there is any error in any part of the composition, it will arrive here
      case Failure(exception) => println(exception)

    }





}

package com.bebit.scala.templates

import monix.eval.Task
import org.scalatest.flatspec.AsyncFlatSpec

import scala.collection.mutable

class GeneralTest extends AsyncFlatSpec{
  import monix.execution.Scheduler.Implicits.global

  def addSoon(addends: Int*): Task[Int] = Task { addends.sum }

  behavior of "addSoon"

  it should "eventually compute a sum of passed Ints" in {
    val futureSum: Task[Int] = addSoon(1, 2)
    // You can map assertions onto a Future, then return
    // the resulting Future[Assertion] to ScalaTest:
    val z = futureSum.runToFuture
    z map { sum => assert(sum == 3) }
  }

  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new mutable.Stack[Int]
    stack.push(1)
    stack.push(2)
    assert(stack.pop() === 2)
    assert(stack.pop() === 1)
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new mutable.Stack[String]
    assertThrows[NoSuchElementException] {
      emptyStack.pop()
    }
  }
}

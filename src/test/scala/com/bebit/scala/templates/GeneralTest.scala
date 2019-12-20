package com.bebit.scala.templates

import com.github.tototoshi.csv.CSVReader
import monix.eval.Task
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.flatspec.AsyncFlatSpec

import scala.collection.mutable

class GeneralTest extends AsyncFlatSpec with AsyncMockFactory{
  import monix.execution.Scheduler.Implicits.global

  def addSoon(addends: Int*): Task[Int] = Task { addends.sum }

  behavior of "addSoon"

  // this is a concurrent test
  it should "eventually compute a sum of passed Ints" in {
    val futureSum: Task[Int] = addSoon(1, 2)
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

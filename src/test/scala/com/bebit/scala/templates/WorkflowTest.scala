package com.bebit.scala.templates

import org.scalatest.flatspec.AsyncFlatSpec

class WorkflowTest extends AsyncFlatSpec{
  import Workflow._

  behavior of "parseCSV"

  it should "return number of lines with correct input" in {
    val x = List(
      List("user_id", "123456789", "visit_id", "0")
    )
    assert(parseCSV(x).size == 1)
  }
}

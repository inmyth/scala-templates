package com.bebit.scala.templates.task

import com.github.tototoshi.csv.CSVReader
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.flatspec.AsyncFlatSpec

class WorkflowTest extends AsyncFlatSpec with AsyncMockFactory{
  import com.bebit.scala.templates.task.WorkflowRecovery._

  behavior of "parseCSV"

  it should "return number of lines with correct input" in {
    val x = List(
      List("user_id", "123456789", "visit_id", "0")
    )
    assert(parseCSV(x).size == 1)
  }

  "parseCSV" should "parse from CSVReader" in {
    val mockedCSVReader = mock[CSVReader]

    // Set expectations
    (mockedCSVReader.all _).expects().returning(
      List (
        List("user_id", "123456789", "visit_id", "0"),
        List("user_id", "666666666", "visit_id", "0")
      )
    )

    assert(mockedCSVReader.all().map(toDeleteKey).head.userId == "user_id")
  }
}

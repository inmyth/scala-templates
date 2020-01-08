package com.bebit.scala.templates.utils

import java.io.File
import java.sql.Timestamp

import com.github.tototoshi.csv.{CSVFormat, CSVWriter, QUOTE_ALL, QUOTE_MINIMAL, Quoting}

import scala.util.Random

object CSVGenerator extends App{

  val f = new File("out.csv")
  val writer = CSVWriter.open(f)(new CSVFormat {

    val delimiter: Char = ','

    val quoteChar: Char = '"'

    val escapeChar: Char = '"'

    val lineTerminator: String = "\r\n"

    val quoting: Quoting = QUOTE_ALL

    val treatEmptyLineAsNil: Boolean = false
  })

  genCliT30(10000, writer)

  writer.close()

  def genCliT30(n: Int, writer: CSVWriter) = {
    import java.text.SimpleDateFormat

    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val rangebegin: Long = Timestamp.valueOf("2019-01-01 00:00:00").getTime
    val rangeend: Long = Timestamp.valueOf("2019-12-31 00:58:00").getTime
    val diff: Long = rangeend - rangebegin + 1

    val list = List("サービスID", "データID", "取得日", "飲食購入金額", "コメント") +:
      (1 to n).map(p => {
      val serviceId = s"0000${Random.between(0, 999999)}"
      val dateAcquisition = format.format(new Timestamp(rangebegin + (Math.random * diff).toLong))
      val price = 50 * Random.between(2, 20)
      List(serviceId, "cli_t-30", dateAcquisition, price, "")
    })
    writer.writeAll(list)
  }




}

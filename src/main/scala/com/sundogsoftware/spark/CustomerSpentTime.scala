package com.sundogsoftware.spark

import org.apache.log4j._
import org.apache.spark.SparkContext

object CustomerSpentTime {
  def parseLine(line: String)={
    val fields = line.split(",")
    val id = fields(0)
    val timeSpent = fields(2).toFloat

    (id, timeSpent)
  }

  def main(args: Array[String])={
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "CustomersSpentTime")

    val rawData = sc.textFile("./customer-orders.csv")
    val parsedData = rawData.map(parseLine)
    val result = parsedData.reduceByKey((x, y) => (x + y)).map(x => (x._2, x._1)).collect()

    for(user <- result.sorted){
      val id = user._2
      val duration = user._1

      println(s"User with $id spent $duration time")
    }
  }

}

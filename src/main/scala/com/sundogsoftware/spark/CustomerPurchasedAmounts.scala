package com.sundogsoftware.spark

import org.apache.log4j._
import org.apache.spark.SparkContext

object CustomerPurchasedAmounts {
  def parseLine(line: String) = {
    val fields = line.split(",")
    val id = fields(0)
    val purchasedAmount = fields(1).toFloat

    (id, purchasedAmount)
  }

  def main(args: Array[String]) = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "CustomerPurchaseAmount")
    val rawData = sc.textFile("./customer-orders.csv")
    val parsedData = rawData.map(parseLine).reduceByKey((x, y) => (x + y)).map(x => (x._2, x._1)).collect()

    for(userInfo <- parsedData.sorted){
      val id = userInfo._2
      val amount = userInfo._1

      println(s"User $id purchased amount: $amount")
    }
  }
}

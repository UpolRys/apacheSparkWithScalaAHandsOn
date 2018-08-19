package com.sundogsoftware.spark

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext

object countWordsBetterSorted {
  def main(arguments:Array[String])={
  Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = new SparkContext("local[*]", "CountWordsBetterVersion")
    val data = sc.textFile("./book.txt")

    val rawData = data.flatMap(x => x.split("\\W+")).map(x => x.toLowerCase())
    val countedUniqueWords = rawData.map(x => (x, 1)).reduceByKey((x, y) => x + y)
    val flipedKeyWithValue = countedUniqueWords.map(x => (x._2, x._1)).collect()

    flipedKeyWithValue.sorted.foreach(println)
  }
}

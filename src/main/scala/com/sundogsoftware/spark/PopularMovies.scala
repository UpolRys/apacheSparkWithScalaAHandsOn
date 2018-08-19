package com.sundogsoftware.spark

import org.apache.spark.SparkContext
import org.apache.log4j._

object PopularMovies {
  def main(args: Array[String])={
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "PopularMovies")

    val rawData = sc.textFile("./ml-100k/u.data")
    val movies = rawData.map(x => (x.split('\t')(1).toInt, 1))

    val popularMovies = movies.reduceByKey((x, y) => (x+y))
    val flipped = popularMovies.map(x => (x._2, x._1)).collect()

    flipped.sorted.foreach(println)
  }
}

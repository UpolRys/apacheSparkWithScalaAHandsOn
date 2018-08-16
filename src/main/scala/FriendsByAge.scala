package com.sundogsoftware.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._

object FriedsByAge {

  def parseLine(line:String) = {
    val tmp = line.split(',')
    (tmp(2).toInt, tmp(3).toInt)
  }

  def main(arguments:Array[String]) =  {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "FriendsByName")

    val lines = sc.textFile("./fakefriends.csv")

    var formatedData = lines.map(line=>parseLine(line))
    val groupedData = formatedData.mapValues(x=>(x, 1)).reduceByKey((x, y) => (x._1+y._1, x._2+y._2))
    val preResult = groupedData.mapValues(x=>x._1/x._2)
    val result = preResult.collect()

    result.sorted.foreach(println)
  }
}

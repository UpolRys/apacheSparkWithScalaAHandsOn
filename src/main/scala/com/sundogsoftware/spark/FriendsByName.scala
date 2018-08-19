package com.sundogsoftware.spark

import org.apache.log4j._
import org.apache.spark.SparkContext

object MyFriendByName {

  def parseLine(line:String)={
    val fields = line.split(",")
    (fields(1), fields(3).toInt)
  }

  def main(args:Array[String]) ={
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = new SparkContext("local[*]", "MyFriendByName")
    val data = sc.textFile("./fakefriends.csv")

    val formatedData = data.map(parseLine)
    val tuples = formatedData.mapValues(numberOfFriends=>(numberOfFriends, 1)).reduceByKey((x, y)=>(x._1+y._1, x._2+y._2))

    val result = tuples.mapValues(x=>x._1/x._2).collect()
    result.sorted.foreach(println)
  }
}

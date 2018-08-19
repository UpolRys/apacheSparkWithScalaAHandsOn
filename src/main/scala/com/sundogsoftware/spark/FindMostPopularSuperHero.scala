package com.sundogsoftware.spark

import org.apache.spark.SparkContext
import org.apache.log4j._

object FindMostPopularSuperHero {
  //Option type is used when you want also return None/Nil kind of types in spark
  def parseNames(line: String) :Option[(Int, String)] = {
    val fields = line.split("\"")

    if(fields.length > 1){
      return Some(fields(0).trim().toInt, fields(1))
    }

    return None
  }

  def parseGraph(line:String)= {
    val fields = line.split("\\s+")

    (fields(0).trim().toInt, fields.length - 1)
  }

  def main(args: Array[String]) = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sc = new SparkContext("local[*]", "FindMostPopularSuperHero")

    val heroNames = sc.textFile("./Marvel-names.txt")
    //here we used flatMap instead of map because flatMap eliminates data with None/Nil type
    val parsedNameWithId = heroNames.flatMap(parseNames)

    val heroGraphs = sc.textFile("./Marvel-graph.txt")
    val parsedHeros = heroGraphs.map(parseGraph)

    val flipped = parsedHeros.map(x => (x._2, x._1))

    //will get the maximum according to key
    val mostPopularSuperHero = flipped.max()

    val popularHeroName = parsedNameWithId.lookup(mostPopularSuperHero._2)(0)
    println(s"Most popular hero is $popularHeroName with connection ${mostPopularSuperHero._1}")
  }
}

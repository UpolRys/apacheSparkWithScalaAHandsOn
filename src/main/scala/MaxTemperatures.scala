import org.apache.spark.SparkContext
import org.apache.log4j._
import scala.math.max

object MaxTemperatures {
  def parseLine(line:String)= {
    val fields = line.split(",")
    val station = fields(0)
    val typeOfTemperature = fields(2)
    val temperature = fields(3).toFloat* 0.1f * (9.0f / 5.0f) + 32.0f

    (station, typeOfTemperature, temperature)
  }



  def main(args: Array[String])= {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = new SparkContext("local[*]", "MaxTemperatureStation")

    val data = sc.textFile("./1800.csv")
    val parsedData = data.map(parseLine)

    val onlyMaximums = parsedData.filter(x => x._2 == "TMAX")

    val maximumOfAStation = onlyMaximums.map(x => (x._1, x._3)).reduceByKey((x,y) => max(x, y)).collect()

//    maximumOfAStation.collect().foreach(println)

    for(result <- maximumOfAStation.sorted){
      val temp = result._2
      val temperature = f"$temp%.2f F"
      val station = result._1
      println(s"$station maximum temperature: $temperature")
    }
  }
}

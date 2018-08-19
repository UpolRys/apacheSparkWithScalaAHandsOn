import org.apache.spark.SparkContext
import org.apache.log4j._

object countWords {
  def main(args: Array[String])= {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val sc = new SparkContext("local[*]", "CountWordsOfABook")

    val rawData = sc.textFile("./book.txt")

    //val words = rawData.flatMap(line => line.split(" "))
    val words = rawData.flatMap(line => line.split("\\W+")).map(x=>x.toLowerCase())

    val countedWords = words.countByValue()

    countedWords.foreach(println)
  }
}

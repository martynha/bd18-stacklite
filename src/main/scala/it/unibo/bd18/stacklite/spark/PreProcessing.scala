package it.unibo.bd18.stacklite.spark

import java.util.Date

import org.apache.spark.{HashPartitioner, SparkConf}

object PreProcessing extends StackliteApp {

  import implicits._
  import it.unibo.bd18.stacklite.Utils.df
  import it.unibo.bd18.util.implicits._

  override protected[this] val conf: SparkConf = new SparkConf().setAppName("PreProcessing")

  val questionsDestPath = args(2)
  val questionTagsDestPath = args(3)

  deleteIfExists(questionsDestPath)
  deleteIfExists(questionTagsDestPath)

  def startDate: Date = df.parse("2012-01-01T00:00:00Z")

  def endDate: Date = df.parse("2016-12-31T23:59:59Z")

  val (questions, questionTags) = questionsRDD
    .filter(_.creationDate.between(startDate, endDate))
    .keyBy(_.id)
    .join(questionTagsRDD.keyBy(_.id))
    .partitionBy(new HashPartitioner(sc.coreCount))
    .map(_._2)
    .collect
    .unzip

  spark.readCSVHeader(args(0))
    .union(questions
      .toSeq
      .toRDD
      .map(_.toCSVString))
    .saveAsTextFile(questionsDestPath)
  spark.readCSVHeader(args(1))
    .union(questionTags
      .toSeq
      .toRDD
      .map(_.toCSVString))
    .saveAsTextFile(questionTagsDestPath)

}

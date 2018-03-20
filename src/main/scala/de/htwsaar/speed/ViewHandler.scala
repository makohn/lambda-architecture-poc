package de.htwsaar.speed

import de.htwsaar.util.Constants
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.streaming.Time
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.sql.functions._

/**
  * Performs ad-hoc view calculation on given DStream.
  */
object ViewHandler {

  def createAllView(sparkContext: SparkContext, tweets: DStream[String]) = {
    createViewForHashtagCount(sparkContext, tweets)
  }

  def createViewForHashtagCount(sparkContext: SparkContext, tweets: DStream[String]) = {

    tweets.foreachRDD { (rdd: RDD[String], time: Time) =>
      val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
      val hashtags: DataFrame = spark.sqlContext.read.json(rdd)
      hashtags.createOrReplaceTempView(Constants.topic)
      val hashtagCountsDF: DataFrame = hashtags.groupBy("hashtag").count()
      val result = hashtagCountsDF.withColumn("timestamp", lit(System.currentTimeMillis))
      result.write.mode(SaveMode.Append)
        .format("org.apache.spark.sql.cassandra")
        .options(Map( "table" -> "hashtagview", "keyspace" -> "realtime_view"))
        .save()
      result.show(false)
      result.printSchema()

    }
  }
}
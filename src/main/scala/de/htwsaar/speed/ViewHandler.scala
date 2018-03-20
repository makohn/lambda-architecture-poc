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

  /**
    * Calculates the top hashtags on a current (time/space bounded) batch of the incoming stream
    * @param sparkContext -  the instance is a Spark application, enabling you to create RDDs etc.
    * @param data - the given data (hashtags from the stream)
    */
  def createViewForHashtagCount(sparkContext: SparkContext, data: DStream[String]) = {

    data.foreachRDD { (rdd: RDD[String], time: Time) =>
      // Initialize a Spark Streaming Session
      val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
      // Transform RDD into JSON
      val hashtags: DataFrame = spark.sqlContext.read.json(rdd)
      // Create a temporary (spark) view for hashtags
      hashtags.createOrReplaceTempView(Constants.topic)
      // Group hashtags with the same key and assign them with their overall count (no. of occurrences)
      val hashtagCountsDF: DataFrame = hashtags.groupBy("hashtag").count()
      // Assign each hashtag record with the timestamp of this calculation iteration
      val result = hashtagCountsDF.withColumn("timestamp", lit(System.currentTimeMillis))
      // Write the results to cassandra
      result.write.mode(SaveMode.Append)
        .format("org.apache.spark.sql.cassandra")
        .options(Map( "table" -> "hashtagview", "keyspace" -> "realtime_view"))
        .save()
      result.show(false) // Do no log the result (enable for debugging purposes)
      result.printSchema() // But log the scheme (for debugging purposes)

    }
  }
}
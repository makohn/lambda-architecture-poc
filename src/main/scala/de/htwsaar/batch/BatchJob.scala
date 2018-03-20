package de.htwsaar.batch

import com.datastax.spark.connector._
import org.apache.spark.{SparkConf, SparkContext}
import org.fusesource.jansi.Ansi.Color._
import org.fusesource.jansi.Ansi._

class BatchJob {

  val sparkConf = new SparkConf()
    .setAppName("Lambda_Batch_Processor").setMaster("local[2]")
    .set("spark.cassandra.connection.host", "127.0.0.1")
    .set("spark.cassandra.auth.username", "cassandra")

  val sc = new SparkContext(sparkConf)

  def start: Unit ={
    val now = System.currentTimeMillis()
    println(ansi().fg(GREEN).a(now).reset())
    val rdd = sc.cassandraTable("master_dataset", "hashtags")
    val hashtags = rdd.select("hashtag")
    val hashtagCounts = hashtags
          .map( row => row.columnValues(0)) // Get the raw hashtag value from the row
          .map( k => (k,1))                 // Assign a count to each hashtag
          .reduceByKey(_+_)                 // Perform MapReduce Algorithm to sum up counts for identical keys
    val result = hashtagCounts
          .map( r => (r._1, r._2, now))     // Assign a timestamp to the dataframe
    //result.foreach(println)
    result.saveToCassandra("batch_view", "hashtagview", SomeColumns("hashtag", "count", "timestamp"))
  }



}





package de.htwsaar.batch

import com.datastax.spark.connector._
import org.apache.spark.{SparkConf, SparkContext}
import org.fusesource.jansi.Ansi.Color._
import org.fusesource.jansi.Ansi._
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer

/**
  * Responsible for calculating batch views.
  */
class BatchJob {

  val sparkConf = new SparkConf()
    .setAppName("Lambda_Batch_Processor").setMaster("local[2]")
    .set("spark.cassandra.connection.host", "127.0.0.1")
    .set("spark.cassandra.auth.username", "cassandra")

  val sc = new SparkContext(sparkConf)

  /**
    * Start calculating top hashtags on master dataset using MapReduce algorithm.
    */
  def start: Unit ={
    val now = System.currentTimeMillis()
    println(ansi().fg(GREEN).a(now).reset())
    val data = sc.cassandraTable("master_dataset", "hashtags")
    val hashtags = data.select("hashtag")
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





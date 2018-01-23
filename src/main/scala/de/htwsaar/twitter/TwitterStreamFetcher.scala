package de.htwsaar.twitter

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter._
import de.htwsaar.util.ConfigUtil._

object TwitterStreamFetcher extends App {

  // Configure Twitter credentials
  setupTwitter()

  // Setup Spark Streaming Context
  val ssc = new StreamingContext("local[*]", "TwitterStreamFetcher", Seconds(1))

  // Create DStream from Twitter
  val tweets = TwitterUtils.createStream(ssc, None)

  // Extract the text of each status
  val statuses = tweets.map(status => status.getText())

  statuses.print()
  ssc.start()
  ssc.awaitTermination()
}

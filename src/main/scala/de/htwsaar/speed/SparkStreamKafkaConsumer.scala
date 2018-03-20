package de.htwsaar.speed

import _root_.kafka.serializer.StringDecoder
import de.htwsaar.consumer.CassandraConnection
import de.htwsaar.util.Constants
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils

/**
  * Consumes data from Kafka and sends them to a real-time processing unit.
  */
object SparkStreamingKafkaConsumer extends App {

  // Kafka
  val brokers = "localhost:9092"

  val sparkConf = new SparkConf().setAppName("KafkaDirectStreaming").setMaster("local[2]")
    .set("spark.cassandra.connection.host", "127.0.0.1")
    .set("spark.cassandra.auth.username", "cassandra")
  val ssc = new StreamingContext(sparkConf, Seconds(20)) // Perform real-time calculation every 20 seconds
  ssc.checkpoint("checkpointDir")

  val topicsSet = Set(Constants.topic)
  val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers, "group.id" -> "spark_streaming")
  val messages: InputDStream[(String, String)] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

  val tweets: DStream[String] = messages.map { case (key, message) => message }
  ViewHandler.createAllView(ssc.sparkContext, tweets)
  ssc.start()
  ssc.awaitTermination()
}
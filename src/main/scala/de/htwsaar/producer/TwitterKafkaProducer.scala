package de.htwsaar.producer

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

/**
  * Publish a stream of records to a Kafka topic.
  * Find the properties here: http://kafka.apache.org/documentation.html#producerconfigs
  */
object TwitterKafkaProducer {

  private val retries: Integer = 0
  private val linger: Integer = 1
  private val buffer: Integer = 33554432

  private val properties = new Properties()
  // address(es) of the Kafka brokers
  properties.put("bootstrap.servers", "localhost:9092")
  // wait for the full set of in-sync replicas to acknowledge the record
  properties.put("acks", "all")
  // value greater than zero will cause the client to resend any record
  properties.put("retries", retries)
  // add up to n ms of latency to records sent in the absence of load
  properties.put("linger.ms", linger)
  // total bytes of memory the producer can use to buffer records
  properties.put("buffer.memory", buffer)
  // serializer class for key
  properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  // serializer class for value
  properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](properties)

  var partition: Integer = 0;

  def send(topic: String, message: String, mode: String = "default"): Boolean = {
    try {
      partition = partition%4
      println(s"write message $message to kafka: topic=$topic partition=$partition")
      producer.send(new ProducerRecord[String, String](topic, partition.toString, message))
      partition += 1
      true
    } catch {
      case ex: Exception =>
       false
    }
  }
}

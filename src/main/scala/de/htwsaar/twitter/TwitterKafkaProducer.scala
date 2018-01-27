package de.htwsaar.twitter

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object TwitterKafkaProducer {

  // As found here:
  // https://kafka.apache.org/090/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html
  private val properties = new Properties()
  properties.put("bootstrap.servers", "localhost:9092")
  properties.put("acks", "all")
  val retries: Integer = 0
  val linger: Integer = 1
  val buffer: Integer = 33554432
  properties.put("retries", retries)
  properties.put("linger.ms", linger)
  properties.put("buffer.memory", buffer)
  properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
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

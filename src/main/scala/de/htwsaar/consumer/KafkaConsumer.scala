package de.htwsaar.consumer

import java.util.Properties

import de.htwsaar.util.Constants
import kafka.consumer.{Consumer, ConsumerConfig, ConsumerIterator, ConsumerTimeoutException}

class KafkaConsumer {

  private val properties = new Properties
  properties.put("group.id", "batch_consumer")
  properties.put("zookeeper.connect", "localhost:2181")
  properties.put("enable.auto.commit", "true")
  properties.put("auto.offset.reset", "smallest")
  properties.put("consumer.timeout.ms", "500")
  properties.put("auto.commit.interval.ms", "1000")
  properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  private val numberStreams= 1 // How many streams for topic
  private val batchSize = 100
  private val topic = Constants.topic
  private val consumerConnector = Consumer.create(new ConsumerConfig(properties))

  private val iterator: ConsumerIterator[Array[Byte], Array[Byte]] =
    consumerConnector
      .createMessageStreams(Map(topic -> numberStreams)) // Create a list of MessageStreams for each topic
      .mapValues(_.head)(topic) // Return the first MessageStream for the topic
      .iterator() // Get an iterator on this message stream

  def read =
    try {
      if (iterator.hasNext()) {
        println("Got message: ")
        readBatchFromTopic(topic, iterator)
      }
    } catch {
      case timeOutEx: ConsumerTimeoutException =>
        println("$$$Getting time out when reading message")
      case ex: Exception =>
        println(s"Not getting message from ", ex)
    }

  /**
    * Reads batches of messages from a kafka broker and writes them into cassandra's master dataset
    * @param topic - the topic which should be observed
    * @param iterator - a MessageStream iterator providing messages from kafka
    */
  private def readBatchFromTopic(topic: String, iterator: ConsumerIterator[Array[Byte], Array[Byte]]): Unit = {
    var batch = List.empty[String]
    while(hasNext(iterator) && batch.size < batchSize) {
      batch = batch :+ (new String(iterator.next().message()))
      if (batch.isEmpty) {
        throw new IllegalArgumentException(s"$topic is  empty")
      } else {
        CassandraOperation.insertHashtags(batch)
      }
        println(s"consumed batch into cassandra : ${batch.length} ${batch.apply(0)}")
    }
  }

  /**
    * Wrapper for iterator hasNext() method in order to catch potentially occuring errors
    * @param it - the iterator that should be checked
    */
  private def hasNext(it: ConsumerIterator[Array[Byte], Array[Byte]]): Boolean =
    try
      it.hasNext()
    catch {
      case timeOutEx: ConsumerTimeoutException =>
        println("Getting time out  when reading message :::::::::::::: ")
        false
      case ex: Exception =>
        println("Getting error when reading message :::::::::::::::::  ", ex)
        false
    }
}

package de.htwsaar.consumer

import akka.actor.{Actor, ActorSystem, Props}
import scala.concurrent.duration.DurationInt

case object Start // Message object

/**
  * Reads data (hashtags) from Kafka and writes them to Cassandra's master dataset.
  * @param consumer - the specified KafkaConsumer instance.
  */
class CassandraConsumer(consumer: KafkaConsumer) extends Actor {

  implicit val dispatcher = context.dispatcher

  val initialDelay = 1000 milli
  val interval = 1 seconds

  // Schedule the writing to the database at the given time intervals
  context.system.scheduler.schedule(initialDelay, interval, self, Start)

  def receive = {
    case Start => consumer.read
  }
}

/**
  * Entry point for the CassandraKafkaConsumer .
  */
object CassandraKafkaConsumer extends App {

  val actorSystem = ActorSystem("KafkaActorSystem")

  val consumer = actorSystem.actorOf(Props(new CassandraConsumer(new KafkaConsumer)))

  consumer ! Start
}

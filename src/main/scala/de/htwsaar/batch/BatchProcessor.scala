package de.htwsaar.batch

import akka.actor.{ActorSystem, Props}

/**
  * Entry point for starting the batch processing.
  */
object BatchProcessor extends App {

  val actorSystem = ActorSystem("BatchActorSystem")

  val processor = actorSystem.actorOf(Props(new BatchProcessingActor(new BatchJob)))

  // Send message (case class) to Actor System
  processor ! StartBatchJob // ! : send a message asynchronously and return immediately

}

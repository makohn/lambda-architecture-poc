package de.htwsaar.batch

import akka.actor.{ActorSystem, Props}

object BatchProcessor extends App {

  val actorSystem = ActorSystem("BatchActorSystem")

  val processor = actorSystem.actorOf(Props(new BatchProcessingActor(new BatchJob)))

  processor ! StartBatchJob

}

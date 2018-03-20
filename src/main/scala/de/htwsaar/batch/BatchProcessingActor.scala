package de.htwsaar.batch

import akka.actor.Actor

import scala.concurrent.duration.DurationInt

case object StartBatchJob

class BatchProcessingActor(job: BatchJob) extends Actor  {

  implicit val dispatcher = context.dispatcher

  val initialDelay = 1000 milli
  val interval = 60 seconds


  context.system.scheduler.schedule(initialDelay, interval, self, StartBatchJob)

  def receive: PartialFunction[Any, Unit] = {

    case StartBatchJob => job.start

  }

}

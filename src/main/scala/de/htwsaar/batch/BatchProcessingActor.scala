package de.htwsaar.batch

import akka.actor.Actor

import scala.concurrent.duration.DurationInt

// Message object
case object StartBatchJob

/**
  * Actor representation of a batch job. Enables batch processing to be scheduled.
  * @param job
  */
class BatchProcessingActor(job: BatchJob) extends Actor  {

  implicit val dispatcher = context.dispatcher

  val initialDelay = 1000 milli
  val interval = 60 seconds

  // Schedule batch processing to be executed in a specified time interval
  context.system.scheduler.schedule(initialDelay, interval, self, StartBatchJob)

  // On Receive message 'StartBatchJob' start calculating the batch view.
  def receive: PartialFunction[Any, Unit] = {
    case StartBatchJob => job.start
  }

}

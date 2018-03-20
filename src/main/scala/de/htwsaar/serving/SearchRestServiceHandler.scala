package de.htwsaar.serving

import akka.util.Timeout
import de.htwsaar.consumer.CassandraOperation
import de.htwsaar.util.{JsonUtil, LoggerUtil}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.Future

/**
  * Handles http requests and delegates them to Cassandra.
  */
trait SearchRestServiceHandler extends JsonUtil with LoggerUtil {

    implicit val timeout = Timeout(40 seconds)

    def getHashtagCountSince(timeInMillis: String, limit: String): Future[String] = {
      Future(write(CassandraOperation.getHashtagCountSince(timeInMillis, limit)))
    }
}

object SearchRestServiceHandler extends SearchRestServiceHandler

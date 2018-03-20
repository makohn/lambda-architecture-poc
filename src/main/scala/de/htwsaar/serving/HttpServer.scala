package de.htwsaar.serving

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.util.{Failure, Success}

/**
  * Creates an HTTP server with an corresponding REST Api.
  */
object HttpServer extends App with SearchRestService {

  implicit val system = ActorSystem("search")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  // Bind the server to localhost, port 9000
  val binding = Http().bindAndHandle(routes, "localhost", 9000)

  binding.onComplete {
    case Success(binding) ⇒
      val localAddress = binding.localAddress
      info(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
    case Failure(e) ⇒
      logger.info(s"Binding failed with ${e.getMessage}")
      system.terminate()
  }
}

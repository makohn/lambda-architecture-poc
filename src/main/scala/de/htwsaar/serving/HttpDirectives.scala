package de.htwsaar.serving

import akka.http.scaladsl.server._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import de.htwsaar.util.LoggerUtil
import org.json4s.{DefaultFormats, Formats, native}

/**
  * Creates routes on a HTTP Server.
  * See here: https://doc.akka.io/docs/akka-http/current/routing-dsl/directives/index.html
  */
trait HttpDirectives extends Directives with Json4sSupport
                                        with LoggerUtil {

  implicit val serialization = native.Serialization

  implicit def json4sFormats: Formats = DefaultFormats

  val rejectionHandler = RejectionHandler.default

  def logDuration(inner: Route): Route = { context =>
    val start = System.currentTimeMillis()
    // handling rejections here so that we get proper status codes
    val innerRejectionsHandled = handleRejections(rejectionHandler)(inner)
    mapResponse { resp =>
      val d = System.currentTimeMillis() - start
      info(s"[${resp.status.intValue()}] ${context.request.method.name} ${context.request.uri} took: ${d}ms")
      resp
    }(innerRejectionsHandled)(context)
  }

}

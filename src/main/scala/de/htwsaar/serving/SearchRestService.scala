package de.htwsaar.serving

import akka.http.scaladsl.server.Route

/**
  * Defines the routes and the get parameters for receiving directed requests.
  */
trait SearchRestService extends HttpDirectives with SearchRestServiceHandler {

  /**
    * Returns the top hashtags for a given timestamp and limit.
    * @return
    */
  def getHashtagCountSince: Route = get {
    path("get" / "hashtags") {
      parameters('timestamp, 'limit) { (timestamp, limit) =>
        logDuration(onSuccess(getHashtagCountSince(timestamp, limit))(complete(_)))
      }
    }
  }

  def routes: Route = getHashtagCountSince
}

package de.htwsaar.twitter

import de.htwsaar.util.Configuration
import net.liftweb.json.{DefaultFormats, Serialization}
import twitter4j._
import twitter4j.conf.ConfigurationBuilder

object TwitterStreamFetcher extends App  {

  // Serialize tweet to JSON
  implicit protected val formats = DefaultFormats
  private def write[T <: AnyRef](value: T): String = Serialization.write(value)

  val cb = new ConfigurationBuilder()
  cb.setDebugEnabled(true)
      .setOAuthConsumerKey(Configuration.consumerKey)
      .setOAuthConsumerSecret(Configuration.consumerSecret)
      .setOAuthAccessToken(Configuration.accessToken)
      .setOAuthAccessTokenSecret(Configuration.accessTokenSecret)

  val twitterStream = new TwitterStreamFactory(cb.build()).getInstance()

  twitterStream.addListener(new StatusListener {

    override def onStatus(status: Status): Unit = {
      val message = new Tweet(status.getId,
        status.getCreatedAt.getTime,
        status.getUser.getName,
        status.getPlace.getCountry)
      TwitterKafkaProducer.send("tweets", write(message))
    }

    override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}

    override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {}

    override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = {}

    override def onStallWarning(warning: StallWarning): Unit = {}

    override def onException(ex: Exception): Unit = {}
  })

  // TODO: need to replace with sensible filter
  twitterStream.sample()

}

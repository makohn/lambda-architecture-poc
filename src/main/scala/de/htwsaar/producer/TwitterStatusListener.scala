package de.htwsaar.producer

import de.htwsaar.util.Constants
import net.liftweb.json.{DefaultFormats, Serialization}
import twitter4j._

/**
  * Listens to new tweets and sends them to Kafka.
  */
class TwitterStatusListener extends StatusListener {
  override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}

  override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {}

  /**
    * Create a new Hashtag object whenever a new tweet arrives.
    * @param status - the twitter4j representation of a new tweet.
    */
  override def onStatus(status: Status): Unit = {

    for (hashtag <- status.getHashtagEntities) {
      val message = write(Hashtag(
        status.getId,
        status.getCreatedAt.getTime,
        status.getUser.getScreenName,
        hashtag.getText
      ))
      println(message)
      TwitterKafkaProducer.send(Constants.topic , message)
    }
  }

  override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = {}

  override def onStallWarning(warning: StallWarning): Unit = {}

  override def onException(ex: Exception): Unit = {}

  // Serialize tweet to JSON
  implicit protected val formats = DefaultFormats
  private def write[T <: AnyRef](value: T): String = Serialization.write(value)
}

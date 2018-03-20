package de.htwsaar.producer

import de.htwsaar.util.{Constants, TwitterConfigUtil}
import twitter4j._
import twitter4j.conf.ConfigurationBuilder

/**
  * Provides a stream of tweets
  */
object TwitterStreamFetcher extends App  {

  // Initialize twitter stream with the corresponding OAuth configuration
  val cb = new ConfigurationBuilder()
  cb.setDebugEnabled(true)
      .setOAuthConsumerKey(TwitterConfigUtil.consumerKey)
      .setOAuthConsumerSecret(TwitterConfigUtil.consumerSecret)
      .setOAuthAccessToken(TwitterConfigUtil.accessToken)
      .setOAuthAccessTokenSecret(TwitterConfigUtil.accessTokenSecret)

  val twitterStream = new TwitterStreamFactory(cb.build()).getInstance()

  // Adding a listener to the stream to allow event-based reactions
  twitterStream.addListener(new TwitterStatusListener());

  // Initialize a location-based filter with min/max values for longitude/latitude
  val minLongLat: Array[Double] = Array(Constants.minLongitude, Constants.minLatitude)
  val maxLongLat: Array[Double] = Array(Constants.maxLongitude, Constants.maxLatitude)
  val filter = new FilterQuery()
  filter.locations(minLongLat, maxLongLat)

  // Start streaming the api
  twitterStream.filter(filter)
}

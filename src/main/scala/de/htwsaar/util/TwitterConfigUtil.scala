package de.htwsaar.util

import com.typesafe.config.ConfigFactory

/**
  * Reads twitter4j configuration from config file
  */
object TwitterConfigUtil {

  val config = ConfigFactory.load()

  val consumerKey = config.getString("twitter.consumerKey")
  val consumerSecret = config.getString("twitter.consumerSecret")
  val accessToken = config.getString("twitter.accessToken")
  val accessTokenSecret = config.getString("twitter.accessTokenSecret")
}

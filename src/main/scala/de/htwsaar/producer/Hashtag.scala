package de.htwsaar.producer

/**
  * Model class for a hashtag.
  */
case class Hashtag (
                     tweetId: Long,
                     createdAt: Long,
                     userName: String,
                     hashtag: String
                   )

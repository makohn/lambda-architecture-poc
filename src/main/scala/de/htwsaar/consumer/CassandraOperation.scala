package de.htwsaar.consumer

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer

case class CountedHashtag(hashtag: String, count: Long)

case class Response(data: List[CountedHashtag])

/**
  * Represents various operations on the cassandra database.
  */
object CassandraOperation extends CassandraConnection {

  /**
    * Insert hashtags into the database (master dataset).
    * @param listJson - A list of json representations of the collected hashtag entities.
    * @param tableName - The table to put the data in.
    */
  def insertHashtags(listJson: List[String], tableName: String = "Hashtags") = {
    println(">>> batch inserted")
    listJson.map(json => cassandraConn.execute(s"INSERT INTO $tableName JSON '$json'"))
  }

  /**
    * Get the top n hashtags since a given timestamp.
    * @param timeInMillis - the timestamp to start the query from
    * @param limit - a limit n to show the n top hashtags
    * @param tableName - the table to process the query on
    */
  def getHashtagCountSince(timeInMillis: String, limit: String, tableName: String = "hashtags"): Response = {
    val millis = timeInMillis.toLong
    val limitLong = limit.toLong
    // Get the most recent hashtag in batch view
    val max_ts = cassandraConn.execute(
      s"SELECT max(timestamp) FROM batch_view.hashtagview;").last.getLong(0)
    // Get the data from the batch view
    val batchViewResult = cassandraConn.execute(
      s"SELECT * FROM batch_view.hashtagview WHERE timestamp = ${max_ts} LIMIT ${limit} ALLOW FILTERING;")
      .all().toList
    // Get the data from the realtime view
    val realTimeViewResult = cassandraConn.execute(
      s"SELECT * FROM realtime_view.hashtagview where timestamp >= ${max_ts} LIMIT ${limit} ALLOW FILTERING;")
      .all().toList

    val countedHashtags: ListBuffer[CountedHashtag] = ListBuffer()

    // Turn the results from the batch view query into CountedHashtag objects
    batchViewResult.map { row =>
      countedHashtags += CountedHashtag(
        row.getString("hashtag"),
        row.getLong("count"))
    }

    // Turn the results from the realtime view query into CountedHashtag objects
    realTimeViewResult.map { row =>
      countedHashtags += CountedHashtag(
        row.getString("hashtag"),
        row.getLong("count"))
    }

    // Merge the results by grouping hashtags with the same key and adding their count
    val result = countedHashtags.groupBy(_.hashtag).map(x => CountedHashtag(x._1,x._2.size)).toList
    Response(countedHashtags.toList)
  }
}

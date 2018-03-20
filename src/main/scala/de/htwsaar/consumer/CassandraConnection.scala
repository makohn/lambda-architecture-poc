package de.htwsaar.consumer

import com.datastax.driver.core._
import de.htwsaar.util.CassandraConfigUtil._
import de.htwsaar.util.Constants

/**
  * Establishes a connection to the Cassandra database and creates keyspaces
  * and tables if they don't exist yet.
  */
trait CassandraConnection {

  val defaultConsistencyLevel = ConsistencyLevel.valueOf(writeConsistency)

  // Create master dataset (keyspace)
 val cassandraConn: Session = {
    val cluster = new Cluster.Builder().withClusterName(Constants.cassandraCluster).
      addContactPoints(hosts.toArray: _*).
      withPort(port).
      withQueryOptions(new QueryOptions().setConsistencyLevel(defaultConsistencyLevel)).build
    val session = cluster.connect
    session.execute(
      s"CREATE KEYSPACE IF NOT EXISTS  ${cassandraKeyspaces.get(0)} WITH REPLICATION = " +
      s"{ 'class' : 'SimpleStrategy', 'replication_factor' : ${replicationFactor} }")
    session.execute(s"USE ${cassandraKeyspaces.get(0)}")
    val query =
      s"CREATE TABLE IF NOT EXISTS Hashtags "                         +
      s"(tweetId bigint, createdAt bigint, userName text, "           +
      s"hashtag text, PRIMARY KEY (hashtag, tweetId, createdAt)) "
    createTables(session, query)
    session
  }

  // Create batchview dataset (keyspace)
  val cassandraConn1: Session = {
    val cluster = new Cluster.Builder().withClusterName(Constants.cassandraCluster).
      addContactPoints(hosts.toArray: _*).
      withPort(port).
      withQueryOptions(new QueryOptions().setConsistencyLevel(defaultConsistencyLevel)).build
    val session = cluster.connect
    session.execute(
      s"CREATE KEYSPACE IF NOT EXISTS  ${cassandraKeyspaces.get(1)} WITH REPLICATION = " +
        s"{ 'class' : 'SimpleStrategy', 'replication_factor' : ${replicationFactor} }")
    session.execute(s"USE ${cassandraKeyspaces.get(1)}")
    val query =
        s"CREATE TABLE IF NOT EXISTS hashtagview "                         +
        s"(hashtag text, " + "count bigint, "  + "timestamp bigint, "      +
        s" PRIMARY KEY (timestamp, count, hashtag)) "                      +
        s"WITH CLUSTERING ORDER BY (count DESC, hashtag ASC)"
    createTables(session, query)
    session
  }

  // Create realtime dataset (keyspace)
  val cassandraConn2: Session = {
    val cluster = new Cluster.Builder().withClusterName(Constants.cassandraCluster).
      addContactPoints(hosts.toArray: _*).
      withPort(port).
      withQueryOptions(new QueryOptions().setConsistencyLevel(defaultConsistencyLevel)).build
    val session = cluster.connect
    session.execute(
      s"CREATE KEYSPACE IF NOT EXISTS  ${cassandraKeyspaces.get(2)} WITH REPLICATION = " +
      s"{ 'class' : 'SimpleStrategy', 'replication_factor' : ${replicationFactor} }")
    session.execute(s"USE ${cassandraKeyspaces.get(2)}")
    val query =
      s"CREATE TABLE IF NOT EXISTS hashtagview "                         +
      s"(hashtag text, " + "count bigint, "  + "timestamp bigint, "      +
      s" PRIMARY KEY (timestamp, count, hashtag)) "                      +
      s"WITH CLUSTERING ORDER BY (count DESC, hashtag ASC)"
    createTables(session, query)
    session
  }

  def createTables(session: Session, createTableQuery: String): ResultSet = session.execute(createTableQuery)
}

object CassandraConnection extends CassandraConnection
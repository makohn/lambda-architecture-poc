package de.htwsaar.util

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._

object CassandraConfigUtil {

  val config = ConfigFactory.load()

  val port = config.getInt("cassandra.port")
  val hosts = config.getStringList("cassandra.hosts").toList
  val cassandraKeyspaces = config.getStringList("cassandra.keyspaces")
  val replicationFactor = config.getString("cassandra.replication_factor").toInt
  val readConsistency = config.getString("cassandra.read_consistency")
  val writeConsistency = config.getString("cassandra.write_consistency")
}

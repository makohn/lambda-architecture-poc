val sparkCore =               	"org.apache.spark" %% "spark-core" % "2.0.0"
val sparkStreaming =          	"org.apache.spark" %% "spark-streaming" % "2.0.0"
val sparkStreamingKafka =     	"org.apache.spark" %% "spark-streaming-kafka-0-8" % "2.0.0"
val sparkSql =                	"org.apache.spark" %% "spark-sql" % "2.0.0"
val kafka =                   	"org.apache.kafka" %% "kafka" % "0.8.2.1"
val twitter4j =               	"org.twitter4j" % "twitter4j-stream" % "4.0.4"
val akkaHttp =                	"com.typesafe.akka" %% "akka-http-experimental" % "2.4.11"
val lift =                    	"net.liftweb" %% "lift-json" % "2.6"
val sparkCassandraConnect =   	"com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0-M3"
val cassandraDriver =         	"com.datastax.cassandra" % "cassandra-driver-core" % "3.1.0"
val hadoop =			"org.apache.hadoop" % "hadoop-core" % "0.20.2"
val akka  =            		"de.heikoseeberger" %% "akka-http-json4s" % "1.7.0"
val json4s =                  	"org.json4s" %% "json4s-native" %  "3.3.0"
val jansi =                   	"org.fusesource.jansi" %  "jansi" % "1.12"
  

lazy val commonSettings = Seq(
	organization := "de.htwsaar",
	version := "0.1.0-SNAPSHOT",
	scalaVersion := "2.11.8"
)

lazy val root = (project in file("."))
	.settings(
		commonSettings,
		name := "lambda-architecture-poc",
		libraryDependencies ++= Seq(
			sparkCore,
			sparkStreaming,
			sparkStreamingKafka,
			sparkSql,
			kafka,
      			twitter4j,
      			akkaHttp,
			lift,
			sparkCassandraConnect,
			cassandraDriver,
			akka,
			json4s,
			jansi
		)	
	)

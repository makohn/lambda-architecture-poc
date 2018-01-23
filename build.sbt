val sparkCore =               "org.apache.spark" %% "spark-core" % "2.0.0"
val sparkStreaming =          "org.apache.spark" %% "spark-streaming" % "2.0.0"
val sparkStreamingKafka =     "org.apache.spark" %% "spark-streaming-kafka-0-8" % "2.0.0"
val sparkSql =                "org.apache.spark" %% "spark-sql" % "2.0.0"
val kafka =                   "org.apache.kafka" %% "kafka" % "0.8.2.1"
val twitter =                 "org.apache.bahir" %% "spark-streaming-twitter" % "2.0.1"

  

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
      twitter
		)	
	)
# lambda-architecture-poc
A PoC implementation of the **λ**-Architecture for collecting and analysing tweets

[![Build Status](https://travis-ci.com/makohn/lambda-architecture-poc.svg?token=z1kyyNNo3nk7k9bTgxPq&branch=master)](https://travis-ci.com/makohn/lambda-architecture-poc)

# Prerequisites

* Apache Spark 
* Apache Cassandra 
* Apache Kafka
* Twitter4j
* SBT
* Scala 

# Installation (macOS)

It is recommended to use Homebrew for installing the required modules:

To install Homebrew, paste the following into your terminal:

```/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"```

- Install Java 8 

```brew tap caskroom/versions```

```brew cask install java8``` 

- Install Kafka

```brew install kafka```

- Install Cassandra

```brew install cassandra```

- Install SBT

```brew install sbt```

- Install Scala

```brew install scala```

# Twitter4J

Make sure you get the keys for accessing the twitter stream. You need to create an application here:
[https://apps.twitter.com]

Once you have created the application you need to put the following keys in src/main/resources/application.conf:

* Consumer Key
* Consumer Secret
* Access Token
* Access Token Secret

# Setup

Before running our application, we need to start the servers for kafka and cassandra

```brew services start zookeeper```

```brew services start kafka```

```brew services start cassandra```

No we can compile and run our application

```sbt clean compile```

```sbt run```

# Running the application

In order to get the application working correctly, you need to follow a specific order when starting the modules:

1. Start **[3] de.htwsaar.producer.TwitterStreamFetcher** (Fetch tweets from twitter streaming api)
2. Start **[2] de.htwsaar.consumer.CassandraKafkaConsumer** (Write them to master dataset)
3. Start **[1] de.htwsaar.batch.BatchProcessor** (Start the batch processor)
4. Start **[5] de.htwsaar.speed.SparkStreamingKafkaConsumer** (Start the real-time processor)
5. Start **[4] de.htwsaar.serving.HttpServer** (Start the HTTP Server as a serving layer)

You can access the results by typing in: "http://localhost:9000/get/hashtags?timestamp=0&limit=20"

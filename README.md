# lambda-architecture-poc
A PoC implementation of the **λ**-Architecture for collecting and analysing tweets

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

# Setup

Before running our application, we need to start the servers for kafka and cassandra

```brew services start zookeeper```

```brew services start kafka```

```brew services start cassandra```

No we can compile and run our application

```sbt clean compile```

```sbt run```

package de.htwsaar.util

import java.io.InputStream

object ConfigUtil {

  def setupTwitter() = {
    import scala.io.Source
    val stream : InputStream = getClass.getResourceAsStream("/twitter.txt")
    for (line <- Source.fromInputStream(stream).getLines) {
      val fields = line.split(" ")
      if (fields.length == 2) {
        System.setProperty("twitter4j.oauth." + fields(0), fields(1))
      }
    }
  }
}

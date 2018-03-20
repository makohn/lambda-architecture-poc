package de.htwsaar.util

import net.liftweb.json.JsonAST.JNothing
import net.liftweb.json.{DefaultFormats, JValue, Serialization, parse => liftParser}

trait JsonUtil {
  implicit protected val formats = DefaultFormats

  protected def write[T <: AnyRef](value: T): String = Serialization.write(value)

  protected def parse(value: String): JValue = liftParser(value)

  implicit protected def extractOrEmptyString(json: JValue): String = json match {
    case JNothing => ""
    case data => data.extract[String]
  }
}
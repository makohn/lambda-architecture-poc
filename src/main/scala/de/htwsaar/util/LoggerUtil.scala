package de.htwsaar.util

import org.slf4j.{ Logger, LoggerFactory }

/**
  * Helps logging information and errors
  */
trait LoggerUtil {

  val logger: Logger = LoggerFactory.getLogger(this.getClass())

  def info(message: String): Unit = logger.info(message)

  def error(message: String, exception: Throwable): Unit = logger.error(message + " Reason::" + exception.getCause)

}

object LoggerUtil extends LoggerUtil
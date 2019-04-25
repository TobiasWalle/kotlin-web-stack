package com.tobiaswalle.kotlin.web.stack.logging

import org.koin.core.Koin
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import org.slf4j.LoggerFactory

class KoinLogger : Logger() {
  private val slf4jLogger = LoggerFactory.getLogger(Koin::class.java)
  override fun log(level: Level, msg: MESSAGE) {
    when (level) {
      Level.DEBUG -> slf4jLogger.debug(msg)
      Level.ERROR -> slf4jLogger.error(msg)
      Level.INFO -> slf4jLogger.info(msg)
    }
  }
}


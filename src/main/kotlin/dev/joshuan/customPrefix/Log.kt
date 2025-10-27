package dev.joshuan.customPrefix

import org.slf4j.Logger
import org.slf4j.MDC

object Log {
    private lateinit var logger: Logger

    fun init(logger: Logger) {
        this.logger = logger

        MDC.put("pluginName", "Custom Prefix")
    }

    fun debug(message: String) = logOrPrint(message) { logger.debug(it) }

    fun info(message: String) = logOrPrint(message) { logger.info(it) }

    fun warn(message: String) = logOrPrint(message) { logger.warn(it) }

    fun error(message: String) = logOrPrint(message) { logger.error(it) }

    private fun logOrPrint(message: String, logAction: (String) -> Unit) {
        if (::logger.isInitialized) {
            logAction(message)
        } else {
            println("Logger not initialized: $message")
        }
    }
}

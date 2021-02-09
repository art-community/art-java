package io.art.kotlin

import io.art.logging.LoggingModule.logger
import org.apache.logging.log4j.core.Logger
import kotlin.reflect.KClass


inline fun <reified T> T.logger(): Logger = logger(T::class.java)
fun logger(contextClass: KClass<*>): Logger = logger(contextClass.java)
fun logger(contextClass: Class<*>): Logger = logger(contextClass)
fun logger(name: String): Logger = logger(name)

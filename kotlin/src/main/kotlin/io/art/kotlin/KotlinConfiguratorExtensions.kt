package io.art.kotlin

import io.art.configurator.module.ConfiguratorModule.configuration


inline fun <reified T> configuration() = configuration(T::class.java)!!

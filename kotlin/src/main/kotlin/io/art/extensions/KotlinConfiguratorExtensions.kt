package io.art.extensions

import io.art.configurator.module.ConfiguratorModule.configuration
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


class ConfigurationDelegate {
    inline operator fun <reified T> getValue(thisRef: Any?, property: KProperty<*>): T = configuration(T::class.java)
}

fun configuration() = ConfigurationDelegate()
inline fun <reified T> configuration(): T = configuration(T::class.java)
fun <T : Any> configuration(model: KClass<T>): T = configuration(model.java) as T
fun <T : Any, R : Any> configuration(model: KClass<T>, action: T.() -> R): R = action(configuration(model.java) as T)

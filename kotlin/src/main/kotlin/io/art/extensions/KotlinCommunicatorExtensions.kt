package io.art.extensions

import io.art.communicator.module.CommunicatorModule.communicator
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


class CommunicatorDelegate {
    inline operator fun <reified T> getValue(thisRef: Any?, property: KProperty<*>): T = communicator(T::class.java)
}

fun communicator() = CommunicatorDelegate()
inline fun <reified T> communicator(): T = communicator(T::class.java)
fun <T : Any> communicator(proxy: KClass<T>): T = communicator(proxy.java) as T
fun <T : Any, R : Any> communicator(proxy: KClass<T>, action: T.() -> R): R = action(communicator(proxy.java) as T)

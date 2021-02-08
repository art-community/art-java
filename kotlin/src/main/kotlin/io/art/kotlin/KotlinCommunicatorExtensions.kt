package io.art.kotlin

import io.art.communicator.module.CommunicatorModule.communicator

inline fun <reified T> communicator() = communicator(T::class.java)!!

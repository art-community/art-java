package io.art.kotlin

import io.art.json.descriptor.JsonReader.readJson
import io.art.message.pack.descriptor.MessagePackReader.readMessagePack
import io.art.protobuf.descriptor.ProtobufReader.readProtobuf
import io.art.value.module.ValueModule.model

inline fun <reified T> json(data: String): T = model(T::class.java, readJson(data))

inline fun <reified T> protobuf(data: ByteArray): T = model(T::class.java, readProtobuf(data))

inline fun <reified T> messagePack(data: ByteArray): T = model(T::class.java, readMessagePack(data))

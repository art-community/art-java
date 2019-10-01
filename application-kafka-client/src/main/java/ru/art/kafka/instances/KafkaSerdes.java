/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.kafka.instances;

import lombok.experimental.*;
import ru.art.kafka.deserializer.*;
import ru.art.kafka.serde.*;
import ru.art.kafka.serializer.*;

@UtilityClass
public class KafkaSerdes {
    public static final KafkaJsonDeserializer KAFKA_JSON_DESERIALIZER = new KafkaJsonDeserializer();
    public static final KafkaProtobufDeserializer KAFKA_PROTOBUF_DESERIALIZER = new KafkaProtobufDeserializer();
    public static final KafkaMessagePackDeserializer KAFKA_MESSAGE_PACK_DESERIALIZER = new KafkaMessagePackDeserializer();
    public static final KafkaXmlDeserializer KAFKA_XML_DESERIALIZER = new KafkaXmlDeserializer();
    public static final KafkaJsonSerde KAFKA_JSON_SERDE = new KafkaJsonSerde();
    public static final KafkaProtobufSerde KAFKA_PROTOBUF_SERDE = new KafkaProtobufSerde();
    public static final KafkaMessagePackSerde KAFKA_MESSAGE_PACK_SERDE = new KafkaMessagePackSerde();
    public static final KafkaXmlSerde KAFKA_XML_SERDE = new KafkaXmlSerde();
    public static final KafkaJsonSerializer KAFKA_JSON_SERIALIZER = new KafkaJsonSerializer();
    public static final KafkaProtobufSerializer KAFKA_PROTOBUF_SERIALIZER = new KafkaProtobufSerializer();
    public static final KafkaMessagePackSerializer KAFKA_MESSAGE_PACK_SERIALIZER = new KafkaMessagePackSerializer();
    public static final KafkaXmlSerializer KAFKA_XML_SERIALIZER = new KafkaXmlSerializer();
}

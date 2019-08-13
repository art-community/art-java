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

package ru.art.kafka.deserializer;

import org.apache.kafka.common.serialization.Deserializer;
import ru.art.entity.Value;
import ru.art.kafka.exception.KafkaDeserialzerException;
import static ru.art.protobuf.descriptor.ProtobufEntityReader.readProtobuf;
import static ru.art.protobuf.entity.ProtobufValueMessage.ProtobufValue.parseFrom;
import java.util.Map;

public class KafkaProtobufDeserializer implements Deserializer<Value> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Value deserialize(String topic, byte[] data) {
        try {
            return readProtobuf(parseFrom(data));
        } catch (Throwable e) {
            throw new KafkaDeserialzerException(e);
        }
    }

    @Override
    public void close() {

    }
}

/*
 * ART
 *
 * Copyright 2020 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.kafka.deserializer;

import io.art.entity.immutable.*;
import org.apache.kafka.common.serialization.*;
import static io.art.message.pack.descriptor.MessagePackEntityReader.*;
import java.util.*;

public class KafkaMessagePackDeserializer implements Deserializer<Value> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Value deserialize(String topic, byte[] data) {
        return readMessagePack(data);
    }

    @Override
    public void close() {

    }
}

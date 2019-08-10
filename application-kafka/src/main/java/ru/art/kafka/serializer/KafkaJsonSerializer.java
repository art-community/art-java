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

package ru.art.kafka.serializer;

import org.apache.kafka.common.serialization.Serializer;
import ru.art.entity.Value;
import static ru.art.json.descriptor.JsonEntityWriter.writeJson;
import java.util.Map;

public class KafkaJsonSerializer implements Serializer<Value> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Value data) {
        return writeJson(data).getBytes();
    }

    @Override
    public void close() {

    }
}

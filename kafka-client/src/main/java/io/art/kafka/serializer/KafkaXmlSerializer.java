/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.kafka.serializer;

import org.apache.kafka.common.serialization.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.value.immutable.Value.*;
import static io.art.value.xml.XmlEntityFromEntityConverter.*;
import static io.art.xml.descriptor.XmlWriter.*;
import java.util.*;

public class KafkaXmlSerializer implements Serializer<Value> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Value data) {
        if (isEmpty(data)) return EMPTY_BYTES;
        return isXml(data)
                ? writeXmlToBytes(asXml(data))
                : isEntity(data)
                ? writeXmlToBytes(fromEntityAsTags(asEntity(data)))
                : EMPTY_BYTES;
    }

    @Override
    public void close() {

    }
}

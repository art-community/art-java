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

package io.art.value.mapper;

import io.art.core.caster.*;
import io.art.value.immutable.*;
import java.io.*;

public interface ValueToModelMapper<T, V extends Value> extends Serializable {
    T map(V value);

    interface EntityToModelMapper<T> extends ValueToModelMapper<T, Entity> {
    }

    interface ArrayToModelMapper<T> extends ValueToModelMapper<T, ArrayValue> {
    }

    interface PrimitiveToModelMapper<T> extends ValueToModelMapper<T, Primitive> {

    }

    interface BinaryToModelMapper<T> extends ValueToModelMapper<T, BinaryValue> {
    }

    interface XmlToModelMapper<T> extends ValueToModelMapper<T, XmlEntity> {
    }

    static <V1 extends Value, V2 extends Value> ValueToModelMapper<V1, V2> identity() {
        return Caster::cast;
    }
}

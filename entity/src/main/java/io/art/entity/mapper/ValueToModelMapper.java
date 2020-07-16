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

package io.art.entity.mapper;

import io.art.entity.*;
import java.io.*;

public interface ValueToModelMapper<T, V extends Value> extends Serializable {
    T map(V value);

    interface EntityToModelMapper<T> extends ValueToModelMapper<T, Entity> {
    }

    interface CollectionValueToModelMapper<T> extends ValueToModelMapper<T, ArrayValue> {
    }

    interface PrimitiveToModelMapper<T> extends ValueToModelMapper<T, Primitive> {
    }

    interface StringDataPrimitiveToModelMapper<T> extends ValueToModelMapper<T, Primitive> {
    }

    interface XmlEntityToModelMapper<T> extends ValueToModelMapper<T, XmlEntity> {
    }
}

/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.entity.mapper;

import ru.art.entity.*;
import java.io.*;

public interface ValueFromModelMapper<T, V extends Value> extends Serializable {
    V map(T model);

    interface EntityFromModelMapper<T> extends ValueFromModelMapper<T, Entity> {
    }

    interface CollectionFromModelMapper<T> extends ValueFromModelMapper<T, CollectionValue<?>> {
    }

    interface PrimitiveFromModelMapper<T> extends ValueFromModelMapper<T, Primitive> {
    }

    interface StringDataPrimitiveFromModelMapper extends ValueFromModelMapper<String, Primitive> {
    }

    interface StringParametersMapFromModelMapper<T> extends ValueFromModelMapper<T, StringParametersMap> {
    }

    interface XmlEntityFromModelMapper<T> extends ValueFromModelMapper<T, XmlEntity> {
    }
}
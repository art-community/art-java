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

package io.art.value.factory;

import io.art.value.immutable.*;
import lombok.experimental.*;
import static io.art.core.factory.SetFactory.setOf;
import static io.art.value.immutable.Entity.*;
import static java.util.stream.Collectors.toSet;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@UtilityClass
public class EntityFactory {
    public static Entity emptyEntity() {
        return EMPTY;
    }

    public static Entity entity(Set<Primitive> keys, Function<Primitive, ? extends Value> valueProvider) {
        return new Entity(setOf(keys), valueProvider);
    }

    public static Entity stringEntity(Set<String> fields, Function<String, ? extends Value> valueProvider) {
        return new Entity(fields.stream().map(PrimitivesFactory::stringPrimitive).collect(toSet()), key -> valueProvider.apply(key.getString()));
    }

    public static Entity intEntity(Set<Integer> fields, Function<Integer, ? extends Value> valueProvider) {
        return new Entity(fields.stream().map(PrimitivesFactory::intPrimitive).collect(toSet()), key -> valueProvider.apply(key.getInt()));
    }

    public static Entity longEntity(Set<Long> fields, Function<Long, ? extends Value> valueProvider) {
        return new Entity(fields.stream().map(PrimitivesFactory::longPrimitive).collect(toSet()), key -> valueProvider.apply(key.getLong()));
    }

    public static Entity doubleEntity(Set<Double> fields, Function<Double, ? extends Value> valueProvider) {
        return new Entity(fields.stream().map(PrimitivesFactory::doublePrimitive).collect(toSet()), key -> valueProvider.apply(key.getDouble()));
    }

    public static Entity floatEntity(Set<Float> fields, Function<Float, ? extends Value> valueProvider) {
        return new Entity(fields.stream().map(PrimitivesFactory::floatPrimitive).collect(toSet()), key -> valueProvider.apply(key.getFloat()));
    }

    public static Entity boolEntity(Set<Boolean> fields, Function<Boolean, ? extends Value> valueProvider) {
        return new Entity(fields.stream().map(PrimitivesFactory::boolPrimitive).collect(toSet()), key -> valueProvider.apply(key.getBool()));
    }

    public static Entity byteEntity(Set<Byte> fields, Function<Byte, ? extends Value> valueProvider) {
        return new Entity(fields.stream().map(PrimitivesFactory::bytePrimitive).collect(toSet()), key -> valueProvider.apply(key.getByte()));
    }
}

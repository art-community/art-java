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

package io.art.value.mapping;

import io.art.core.annotation.*;
import io.art.value.factory.*;
import io.art.value.immutable.*;
import io.art.value.mapper.ValueFromModelMapper.*;
import io.art.value.mapper.ValueToModelMapper.*;
import lombok.experimental.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.extensions.DateTimeExtensions.*;
import static io.art.value.factory.PrimitivesFactory.*;
import java.time.*;
import java.util.*;

@UtilityClass
@UsedByGenerator
public class PrimitiveMapping {
    public static PrimitiveFromModelMapper<String> fromString = value -> let(value, PrimitivesFactory::stringPrimitive);
    public static PrimitiveToModelMapper<String> toString = primitive -> let(primitive, Primitive::getString);

    public static PrimitiveFromModelMapper<Integer> fromInt = value -> let(value, PrimitivesFactory::intPrimitive);
    public static PrimitiveToModelMapper<Integer> toInt = primitive -> let(primitive, Primitive::getInt);

    public static PrimitiveFromModelMapper<Long> fromLong = value -> let(value, PrimitivesFactory::longPrimitive);
    public static PrimitiveToModelMapper<Long> toLong = primitive -> let(primitive, Primitive::getLong);

    public static PrimitiveFromModelMapper<Double> fromDouble = value -> let(value, PrimitivesFactory::doublePrimitive);
    public static PrimitiveToModelMapper<Double> toDouble = primitive -> let(primitive, Primitive::getDouble);

    public static PrimitiveFromModelMapper<Boolean> fromBool = value -> let(value, PrimitivesFactory::boolPrimitive);
    public static PrimitiveToModelMapper<Boolean> toBool = primitive -> let(primitive, Primitive::getBool);

    public static PrimitiveFromModelMapper<Byte> fromByte = value -> let(value, PrimitivesFactory::bytePrimitive);
    public static PrimitiveToModelMapper<Byte> toByte = primitive -> let(primitive, Primitive::getByte);

    public static PrimitiveFromModelMapper<Float> fromFloat = value -> let(value, PrimitivesFactory::floatPrimitive);
    public static PrimitiveToModelMapper<Float> toFloat = primitive -> let(primitive, Primitive::getFloat);

    public static PrimitiveFromModelMapper<Short> fromShort = value -> let(value, PrimitivesFactory::shortPrimitive);
    public static PrimitiveToModelMapper<Short> toShort = primitive -> let(primitive, Primitive::getShort);

    public static PrimitiveFromModelMapper<Character> fromChar = value -> let(value, PrimitivesFactory::charPrimitive);
    public static PrimitiveToModelMapper<Character> toChar = primitive -> let(primitive, Primitive::getChar);

    public static PrimitiveFromModelMapper<UUID> fromUuid = uuid -> let(uuid, notNull -> stringPrimitive(notNull.toString()));
    public static PrimitiveToModelMapper<UUID> toUuid = uuid -> let(uuid, notNull -> UUID.fromString(notNull.getString()));

    public static PrimitiveFromModelMapper<LocalDateTime> fromLocalDateTime = dateTime -> let(dateTime, notNull -> longPrimitive(toMillis(notNull)));
    public static PrimitiveToModelMapper<LocalDateTime> toLocalDateTime = dateTime -> let(dateTime, notNull -> localFromMillis(notNull.getLong()));

    public static PrimitiveFromModelMapper<ZonedDateTime> fromZonedDateTime = dateTime -> let(dateTime, notNull -> longPrimitive(toMillis(notNull)));
    public static PrimitiveToModelMapper<ZonedDateTime> toZonedDateTime = dateTime -> let(dateTime, notNull -> zonedFromMillis(notNull.getLong()));

    public static PrimitiveFromModelMapper<Date> fromDate = dateTime -> let(dateTime, notNull -> longPrimitive(notNull.getTime()));
    public static PrimitiveToModelMapper<Date> toDate = dateTime -> let(dateTime, notNull -> new Date(notNull.getLong()));

    public static PrimitiveFromModelMapper<Duration> fromDuration = duration -> let(duration, notNull -> longPrimitive(duration.toMillis()));
    public static PrimitiveToModelMapper<Duration> toDuration = duration -> let(duration, notNull -> Duration.ofMillis(duration.getLong()));
}

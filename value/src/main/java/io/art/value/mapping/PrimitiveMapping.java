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
import static io.art.core.extensions.DateTimeExtensions.*;
import static io.art.value.factory.PrimitivesFactory.*;
import java.time.*;
import java.util.*;

@UtilityClass
@UsedByGenerator
public class PrimitiveMapping {
    public static PrimitiveFromModelMapper<String> fromString = PrimitivesFactory::stringPrimitive;
    public static PrimitiveToModelMapper<String> toString = Primitive::getString;

    public static PrimitiveFromModelMapper<Integer> fromInt = PrimitivesFactory::intPrimitive;
    public static PrimitiveToModelMapper<Integer> toInt = Primitive::getInt;

    public static PrimitiveFromModelMapper<Long> fromLong = PrimitivesFactory::longPrimitive;
    public static PrimitiveToModelMapper<Long> toLong = Primitive::getLong;

    public static PrimitiveFromModelMapper<Double> fromDouble = PrimitivesFactory::doublePrimitive;
    public static PrimitiveToModelMapper<Double> toDouble = Primitive::getDouble;

    public static PrimitiveFromModelMapper<Boolean> fromBool = PrimitivesFactory::boolPrimitive;
    public static PrimitiveToModelMapper<Boolean> toBool = Primitive::getBool;

    public static PrimitiveFromModelMapper<Byte> fromByte = PrimitivesFactory::bytePrimitive;
    public static PrimitiveToModelMapper<Byte> toByte = Primitive::getByte;

    public static PrimitiveFromModelMapper<Float> fromFloat = PrimitivesFactory::floatPrimitive;
    public static PrimitiveToModelMapper<Float> toFloat = Primitive::getFloat;

    public static PrimitiveFromModelMapper<Short> fromShort = PrimitivesFactory::shortPrimitive;
    public static PrimitiveToModelMapper<Short> toShort = Primitive::getShort;

    public static PrimitiveFromModelMapper<Character> fromChar = PrimitivesFactory::charPrimitive;
    public static PrimitiveToModelMapper<Character> toChar = Primitive::getChar;

    public static PrimitiveFromModelMapper<UUID> fromUuid = uuid -> stringPrimitive(uuid.toString());
    public static PrimitiveToModelMapper<UUID> toUuid = uuid -> UUID.fromString(uuid.getString());

    public static PrimitiveFromModelMapper<LocalDateTime> fromLocalDateTime = dateTime -> longPrimitive(toMillis(dateTime));
    public static PrimitiveToModelMapper<LocalDateTime> toLocalDateTime = dateTime -> localFromMillis(dateTime.getLong());

    public static PrimitiveFromModelMapper<ZonedDateTime> fromZonedDateTime = dateTime -> longPrimitive(toMillis(dateTime));
    public static PrimitiveToModelMapper<ZonedDateTime> toZonedDateTime = dateTime -> zonedFromMillis(dateTime.getLong());

    public static PrimitiveFromModelMapper<Date> fromDate = dateTime -> longPrimitive(dateTime.getTime());
    public static PrimitiveToModelMapper<Date> toDate = dateTime -> new Date(dateTime.getLong());
}

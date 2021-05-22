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

package io.art.meta.computer;

import io.art.core.collection.*;
import io.art.meta.exception.*;
import io.art.meta.model.*;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.meta.constants.MetaConstants.*;
import static io.art.meta.type.TypeInspector.*;
import static io.art.value.mapper.ValueMapper.*;
import static io.art.value.mapping.ArrayMapping.*;
import static io.art.value.mapping.PrimitiveMapping.*;
import static java.text.MessageFormat.*;
import java.time.*;
import java.util.*;

@UtilityClass
public class MetaTypeMappersComputer {
    public static <T> ValueMapper<T, Value> compute(MetaType<T> type) {
        Class<T> rawType = type.type();
        boolean array = type.array();
        ImmutableSet<MetaType<?>> parameters = type.parameters();
        if (array) {
            MetaType<T> component = type.toBuilder().array(false).build();
            return cast(mapper(fromArray(component.fromModel()), toArrayRaw(component.arrayFactory(), component.toModel())));
        }
        if (isPrimitive(rawType)) {
            if (Short.class.equals(rawType)) {
                return cast(mapper(fromShort, toShort));
            }
            if (Integer.class.equals(rawType)) {
                return cast(mapper(fromInt, toInt));
            }
            if (Long.class.equals(rawType)) {
                return cast(mapper(fromLong, toLong));
            }
            if (Double.class.equals(rawType)) {
                return cast(mapper(fromDouble, toDouble));
            }
            if (Boolean.class.equals(rawType)) {
                return cast(mapper(fromBool, toBool));
            }
            if (Float.class.equals(rawType)) {
                return cast(mapper(fromFloat, toFloat));
            }
            if (Character.class.equals(rawType)) {
                return cast(mapper(fromChar, toChar));
            }
            if (UUID.class.equals(rawType)) {
                return cast(mapper(fromUuid, toUuid));
            }
            if (LocalDateTime.class.equals(rawType)) {
                return cast(mapper(fromLocalDateTime, toLocalDateTime));
            }
            if (ZonedDateTime.class.equals(rawType)) {
                return cast(mapper(fromZonedDateTime, toZonedDateTime));
            }
            if (Date.class.equals(rawType)) {
                return cast(mapper(fromDate, toDate));
            }
            if (Duration.class.equals(rawType)) {
                return cast(mapper(fromDuration, toDuration));
            }
            throw new MetaException(format(UNSUPPORTED_TYPE, type));
        }
        if (isCollection(rawType)) {
            if (isEmpty(parameters)) {
                throw new MetaException(format(UNSUPPORTED_TYPE, type));
            }
            if (isList(rawType)) {

            }
            if (isImmutableArray(rawType)) {

            }
            if (isSet(rawType)) {

            }
            if (isImmutableSet(rawType)) {

            }
        }
        if (isMap(rawType)) {

        }
        throw new MetaException(format(UNSUPPORTED_TYPE, type));
    }
}

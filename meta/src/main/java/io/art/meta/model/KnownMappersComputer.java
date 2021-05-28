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

package io.art.meta.model;

import io.art.core.collection.*;
import io.art.meta.exception.*;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import io.art.value.mapper.ValueFromModelMapper.*;
import io.art.value.mapper.ValueToModelMapper.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.meta.constants.MetaConstants.*;
import static io.art.meta.type.TypeInspector.*;
import static io.art.value.immutable.Value.*;
import static io.art.value.mapper.ValueMapper.*;
import static io.art.value.mapping.ArrayMapping.*;
import static io.art.value.mapping.BinaryMapping.*;
import static io.art.value.mapping.EntityMapping.*;
import static io.art.value.mapping.LazyMapping.*;
import static io.art.value.mapping.OptionalMapping.*;
import static io.art.value.mapping.PrimitiveMapping.*;
import static java.text.MessageFormat.*;
import java.time.*;
import java.util.*;

@UtilityClass
class KnownMappersComputer {
    static <T> ValueMapper<T, Value> computeKnownMappers(MetaType<T> type) {
        Class<T> rawType = type.type();
        boolean array = type.array();
        boolean primitive = type.primitive();
        ImmutableSet<MetaType<?>> parameters = type.parameters();
        if (isByteArray(rawType)) {
            return cast(mapper(fromBinary, toBinary));
        }
        if (array) {
            if (primitive) {
                if (short.class.equals(rawType)) {
                    return cast(mapper(fromShortArray, toShortArray));
                }
                if (int.class.equals(rawType)) {
                    return cast(mapper(fromIntArray, toIntArray));
                }
                if (long.class.equals(rawType)) {
                    return cast(mapper(fromLongArray, toLongArray));
                }
                if (double.class.equals(rawType)) {
                    return cast(mapper(fromDoubleArray, toDoubleArray));
                }
                if (boolean.class.equals(rawType)) {
                    return cast(mapper(fromBoolArray, toBoolArray));
                }
                if (float.class.equals(rawType)) {
                    return cast(mapper(fromFloatArray, toFloatArray));
                }
                if (char.class.equals(rawType)) {
                    return cast(mapper(fromCharArray, toCharArray));
                }
            }
            MetaType<T> component = type.toBuilder().array(false).build().compute();
            return cast(mapper(fromArray(component.fromModel()), toArrayRaw(component.asArray(), component.toModel())));
        }
        if (isPrimitive(rawType)) {
            if (short.class.equals(rawType) || Short.class.equals(rawType)) {
                return cast(mapper(fromShort, toShort));
            }
            if (int.class.equals(rawType) || Integer.class.equals(rawType)) {
                return cast(mapper(fromInt, toInt));
            }
            if (long.class.equals(rawType) || Long.class.equals(rawType)) {
                return cast(mapper(fromLong, toLong));
            }
            if (double.class.equals(rawType) || Double.class.equals(rawType)) {
                return cast(mapper(fromDouble, toDouble));
            }
            if (boolean.class.equals(rawType) || Boolean.class.equals(rawType)) {
                return cast(mapper(fromBool, toBool));
            }
            if (float.class.equals(rawType) || Float.class.equals(rawType)) {
                return cast(mapper(fromFloat, toFloat));
            }
            if (char.class.equals(rawType) || Character.class.equals(rawType)) {
                return cast(mapper(fromChar, toChar));
            }
            if (String.class.equals(rawType)) {
                return cast(mapper(fromString, toString));
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
        if (isObject(rawType) || isValue(rawType)) {
            return cast(mapper(ValueFromModelMapper.identity(), ValueToModelMapper.identity()));
        }
        if (isEmpty(parameters)) {
            throw new MetaException(format(UNSUPPORTED_TYPE, type));
        }
        MetaType<?> firstParameter = parameters.stream()
                .findFirst()
                .map(MetaType::compute)
                .orElseThrow(() -> new MetaException(format(UNSUPPORTED_TYPE, type)));
        if (isLazyValue(rawType)) {
            return mapper(cast(fromLazy(firstParameter.fromModel())), cast(toLazy((ValueToModelMapper<?, Value>) firstParameter.toModel())));
        }
        if (isOptional(rawType)) {
            return mapper(cast(fromOptional(firstParameter.fromModel())), cast(toOptional((ValueToModelMapper<?, Value>) firstParameter.toModel())));
        }
        if (isCollection(rawType)) {
            if (isList(rawType)) {
                return mapper(cast(fromList(firstParameter.fromModel())), cast(toMutableList(firstParameter.toModel())));
            }
            if (isImmutableArray(rawType)) {
                return mapper(cast(fromImmutableArray(firstParameter.fromModel())), cast(toImmutableArray(firstParameter.toModel())));
            }
            if (isSet(rawType)) {
                return mapper(cast(fromSet(firstParameter.fromModel())), cast(toMutableSet(firstParameter.toModel())));
            }
            if (isImmutableSet(rawType)) {
                return mapper(cast(fromImmutableSet(firstParameter.fromModel())), cast(toImmutableSet(firstParameter.toModel())));
            }
            if (isImmutableCollection(rawType)) {
                return mapper(cast(fromCollection(firstParameter.fromModel())), cast(toImmutableArray(firstParameter.toModel())));
            }
            if (isQueue(rawType)) {
                return mapper(cast(fromQueue(firstParameter.fromModel())), cast(toMutableQueue(firstParameter.toModel())));
            }
            if (isDequeue(rawType)) {
                return mapper(cast(fromDeque(firstParameter.fromModel())), cast(toMutableDeque(firstParameter.toModel())));
            }
            if (isStream(rawType)) {
                return mapper(cast(fromStream(firstParameter.fromModel())), cast(toStream(firstParameter.toModel())));
            }
            return mapper(cast(fromCollection(firstParameter.fromModel())), cast(toMutableCollection(firstParameter.toModel())));
        }
        if (isMap(rawType) || isImmutableMap(rawType)) {
            if (parameters.size() != 2) {
                throw new MetaException(format(UNSUPPORTED_TYPE, type));
            }
            MetaType<?>[] metaTypes = parameters.toArray(MetaType[]::new);
            MetaType<?> key = metaTypes[0].compute();
            if (isNotPrimitive(key.type())) {
                throw new MetaException(format(UNSUPPORTED_TYPE, type));
            }

            MetaType<?> value = metaTypes[1].compute();
            PrimitiveToModelMapper<Object> keyToModel = cast(key.toModel());
            PrimitiveFromModelMapper<Object> keyFromModel = model -> asPrimitive(key.fromModel().map(model));
            ValueToModelMapper<Object, Value> valueToModel = cast(value.toModel());
            ValueFromModelMapper<Object, Value> valueFromModel = model -> value.fromModel().map(model);

            if (isImmutableMap(rawType)) {
                EntityFromModelMapper<ImmutableMap<Object, Object>> fromMapper = fromImmutableMap(cast(keyToModel), cast(keyFromModel), valueFromModel);
                EntityToModelMapper<ImmutableMap<Object, Object>> toMapper = toImmutableMap(keyToModel, keyFromModel, valueToModel);
                return mapper(cast(fromMapper), cast(toMapper));
            }

            EntityFromModelMapper<Map<Object, Object>> fromMapper = fromMap(keyToModel, keyFromModel, valueFromModel);
            EntityToModelMapper<Map<Object, Object>> toMapper = toMutableMap(keyToModel, valueToModel);
            return mapper(cast(fromMapper), cast(toMapper));
        }
        throw new MetaException(format(UNSUPPORTED_TYPE, type));
    }
}

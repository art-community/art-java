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

package ru.art.entity;

import ru.art.entity.mapper.ValueFromModelMapper.*;
import ru.art.entity.mapper.*;
import ru.art.entity.mapper.ValueToModelMapper.*;

import static ru.art.core.extension.StringExtensions.*;
import static ru.art.entity.mapper.ValueMapper.*;

public interface StringParametersMapping {
    static ValueMapper<String, StringParametersMap> stringParameterToStringMapper(String key) {
        return mapper(StringParameterMapping.fromModel(key), StringParameterMapping.toModel(key));
    }

    static ValueMapper<Integer, StringParametersMap> stringParameterToIntMapper(String key) {
        return mapper(IntParameterMapping.fromModel(key), IntParameterMapping.toModel(key));
    }

    static ValueMapper<Long, StringParametersMap> stringParameterToLongMapper(String key) {
        return mapper(LongParameterMapping.fromModel(key), LongParameterMapping.toModel(key));
    }

    static ValueMapper<Double, StringParametersMap> stringParameterToDoubleMapper(String key) {
        return mapper(DoubleParameterMapping.fromModel(key), DoubleParameterMapping.toModel(key));
    }

    static ValueMapper<Boolean, StringParametersMap> stringParameterToBoolMapper(String key) {
        return mapper(BooleanParameterMapping.fromModel(key), BooleanParameterMapping.toModel(key));
    }

    static ValueMapper<Byte, StringParametersMap> stringParameterToByteMapper(String key) {
        return mapper(ByteParameterMapping.fromModel(key), ByteParameterMapping.toModel(key));
    }

    static ValueMapper<Float, StringParametersMap> stringParameterToFloatMapper(String key) {
        return mapper(FloatParameterMapping.fromModel(key), FloatParameterMapping.toModel(key));
    }

    interface StringParameterMapping {
        static StringParametersMapFromModelMapper<String> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, str).build();
        }

        static StringParametersMapToModelMapper<String> toModel(String name) {
            return keyValue -> keyValue.getParameters().get(name);
        }
    }

    interface IntParameterMapping {
        static StringParametersMapFromModelMapper<Integer> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, emptyIfNull(str)).build();
        }

        static StringParametersMapToModelMapper<Integer> toModel(String name) {
            return keyValue -> Integer.valueOf(keyValue.getParameters().get(name));
        }
    }

    interface LongParameterMapping {
        static StringParametersMapFromModelMapper<Long> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, emptyIfNull(str)).build();
        }

        static StringParametersMapToModelMapper<Long> toModel(String name) {
            return keyValue -> Long.valueOf(keyValue.getParameters().get(name));
        }
    }

    interface DoubleParameterMapping {
        static StringParametersMapFromModelMapper<Double> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, emptyIfNull(str)).build();
        }

        static StringParametersMapToModelMapper<Double> toModel(String name) {
            return keyValue -> Double.valueOf(keyValue.getParameters().get(name));
        }
    }

    interface BooleanParameterMapping {
        static StringParametersMapFromModelMapper<Boolean> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, emptyIfNull(str)).build();
        }

        static StringParametersMapToModelMapper<Boolean> toModel(String name) {
            return keyValue -> Boolean.valueOf(keyValue.getParameters().get(name));
        }
    }

    interface ByteParameterMapping {
        static StringParametersMapFromModelMapper<Byte> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, emptyIfNull(str)).build();
        }

        static StringParametersMapToModelMapper<Byte> toModel(String name) {
            return keyValue -> Byte.valueOf(keyValue.getParameters().get(name));
        }
    }

    interface FloatParameterMapping {
        static StringParametersMapFromModelMapper<Float> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, emptyIfNull(str)).build();
        }

        static StringParametersMapToModelMapper<Float> toModel(String name) {
            return keyValue -> Float.valueOf(keyValue.getParameters().get(name));
        }
    }
}

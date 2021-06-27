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

package io.art.meta.transformer;

import lombok.experimental.*;
import static io.art.core.constants.DateTimeConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.DateTimeExtensions.*;
import java.time.*;
import java.util.*;

@UtilityClass
public class PrimitiveTransformers {
    public static final MetaTransformer<String> STRING_TRANSFORMER = new MetaTransformer<String>() {
        @Override
        public String transform(Object value) {
            return value.toString();
        }

        public String transform(Date value) {
            return DEFAULT_FORMATTER.format(zonedFromSimpleDate(value));
        }

        public String transform(LocalDateTime value) {
            return DEFAULT_FORMATTER.format(value);
        }

        public String transform(ZonedDateTime value) {
            return DEFAULT_FORMATTER.format(value);
        }

        public String transform(char[] value) {
            return new String(value);
        }

        public String transform(byte[] value) {
            return new String(value, context().configuration().getCharset());
        }

        public String transform(Enum<?> value) {
            return value.name();
        }
    };

    public static final MetaTransformer<Integer> INTEGER_TRANSFORMER = new MetaTransformer<Integer>() {
        public Integer transform(Number value) {
            return value.intValue();
        }

        public Integer transform(String value) {
            return Integer.parseInt(value);
        }

        public Integer transform(Enum<?> value) {
            return value.ordinal();
        }
    };

    public static final MetaTransformer<Float> FLOAT_TRANSFORMER = new MetaTransformer<Float>() {
        public Float transform(Number value) {
            return value.floatValue();
        }

        public Float transform(String value) {
            return Float.parseFloat(value);
        }
    };

    public static final MetaTransformer<Double> DOUBLE_TRANSFORMER = new MetaTransformer<Double>() {
        public Double transform(Number value) {
            return value.doubleValue();
        }

        public Double transform(String value) {
            return Double.parseDouble(value);
        }
    };

    public static final MetaTransformer<Short> SHORT_TRANSFORMER = new MetaTransformer<Short>() {
        public Short transform(Number value) {
            return value.shortValue();
        }

        public Short transform(String value) {
            return Short.parseShort(value);
        }
    };

    public static final MetaTransformer<Long> LONG_TRANSFORMER = new MetaTransformer<Long>() {
        public Long transform(Number value) {
            return value.longValue();
        }

        public Long transform(String value) {
            return Long.parseLong(value);
        }

        public Long transform(Date value) {
            return value.getTime();
        }

        public Long transform(LocalDateTime value) {
            return toMillis(value);
        }

        public Long transform(ZonedDateTime value) {
            return toMillis(value);
        }
    };

    public static final MetaTransformer<Byte> BYTE_TRANSFORMER = new MetaTransformer<Byte>() {
        public Byte transform(Number value) {
            return value.byteValue();
        }

        public Byte transform(String value) {
            return Byte.parseByte(value);
        }
    };

    public static final MetaTransformer<Boolean> BOOLEAN_TRANSFORMER = new MetaTransformer<Boolean>() {
        public Boolean transform(Boolean value) {
            return value;
        }

        public Boolean transform(String value) {
            return Boolean.parseBoolean(value);
        }
    };

    public static final MetaTransformer<Character> CHARACTER_TRANSFORMER = new MetaTransformer<Character>() {
        public Character transform(Character value) {
            return value;
        }
    };

    public static final MetaTransformer<Duration> DURATION_TRANSFORMER = new MetaTransformer<Duration>() {
        public Duration transform(Duration value) {
            return value;
        }

        public Duration transform(Long value) {
            return Duration.ofMillis(value);
        }

        public Duration transform(String value) {
            return Duration.parse(value);
        }
    };

    public static final MetaTransformer<Date> DATE_TRANSFORMER = new MetaTransformer<Date>() {
        public Date transform(Date value) {
            return value;
        }

        public Date transform(Long value) {
            return new Date(value);
        }

        public Date transform(String value) {
            return toSimpleDate(ZonedDateTime.parse(value, DEFAULT_FORMATTER));
        }
    };

    public static final MetaTransformer<LocalDateTime> LOCAL_DATE_TIME_TRANSFORMER = new MetaTransformer<LocalDateTime>() {
        public LocalDateTime transform(LocalDateTime value) {
            return value;
        }

        public LocalDateTime transform(Long value) {
            return localFromMillis(value);
        }

        public LocalDateTime transform(String value) {
            return LocalDateTime.parse(value, DEFAULT_FORMATTER);
        }
    };

    public static final MetaTransformer<ZonedDateTime> ZONED_DATE_TIME_TRANSFORMER = new MetaTransformer<ZonedDateTime>() {
        public ZonedDateTime transform(ZonedDateTime value) {
            return value;
        }

        public ZonedDateTime transform(Long value) {
            return zonedFromMillis(value);
        }

        public ZonedDateTime transform(String value) {
            return ZonedDateTime.parse(value, DEFAULT_FORMATTER);
        }
    };
}

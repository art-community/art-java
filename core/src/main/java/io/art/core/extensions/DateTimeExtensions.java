/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.core.extensions;

import lombok.experimental.*;
import static java.time.Instant.*;
import static java.time.ZoneId.*;
import static java.util.Objects.*;
import java.time.*;
import java.util.*;

@UtilityClass
public class DateTimeExtensions {
    public static ZonedDateTime zonedFromMillis(long millis) {
        return ofEpochMilli(millis).atZone(systemDefault());
    }

    public static LocalDateTime localFromMillis(long millis) {
        return zonedFromMillis(millis).toLocalDateTime();
    }

    public static long toMillis(ZonedDateTime dateTime) {
        if (isNull(dateTime)) {
            return 0L;
        }
        return dateTime.toInstant().toEpochMilli();
    }

    public static long toMillis(LocalDateTime dateTime) {
        if (isNull(dateTime)) {
            return 0L;
        }
        return dateTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }

    public static Date toSimpleDate(ZonedDateTime dateTime) {
        if (isNull(dateTime)) {
            return new Date(0L);
        }
        return new Date(toMillis(dateTime));
    }

    public static Date toSimpleDate(LocalDateTime dateTime) {
        if (isNull(dateTime)) {
            return new Date(0L);
        }
        return new Date(toMillis(dateTime));
    }

    public static ZonedDateTime zonedFromSimpleDate(Date date) {
        return ofEpochMilli(date.getTime()).atZone(systemDefault());
    }

    public static LocalDateTime localFromSimpleDate(Date date) {
        return zonedFromSimpleDate(date).toLocalDateTime();
    }
}

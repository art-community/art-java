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

package io.art.core.extension;

import lombok.experimental.*;
import static io.art.core.context.Context.*;
import static java.time.Instant.*;
import static java.time.ZoneId.*;
import static java.time.format.DateTimeFormatter.*;
import static java.util.Objects.*;
import static java.util.Optional.of;
import static java.util.Optional.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

@UtilityClass
public class DateTimeExtensions {
    public static LocalDateTime parse(String pattern, String string) {
        return parse(pattern, string, contextConfiguration().getLocale());
    }

    public static LocalDateTime parse(String pattern, String string, Locale locale) {
        return LocalDateTime.parse(string, ofPattern(pattern, locale));
    }

    public static LocalDateTime tryParse(String string, DateTimeFormatter format) {
        return tryParse(string, format, null);
    }

    public static LocalDateTime tryParse(String string, DateTimeFormatter format, LocalDateTime orElse) {
        try {
            return LocalDateTime.parse(string, format);
        } catch (Throwable throwable) {
            return orElse;
        }
    }

    public static Optional<LocalDateTime> parseSafety(String pattern, String string, Locale locale) {
        try {
            return of(LocalDateTime.parse(string, ofPattern(pattern, locale)));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return empty();
        }
    }

    public static String format(String template, LocalDateTime date) {
        return format(template, date, contextConfiguration().getLocale());
    }

    public static String tryFormat(LocalDateTime dateTime, DateTimeFormatter format, String orElse) {
        try {
            return format.format(dateTime);
        } catch (Throwable throwable) {
            return orElse;
        }
    }

    public static String tryFormat(LocalDateTime dateTime, DateTimeFormatter format) {
        return tryFormat(dateTime, format, null);
    }

    public static String format(String pattern, LocalDateTime dateTime, Locale locale) {
        return ofPattern(pattern, locale).format(dateTime);
    }

    public static long toMillis(LocalDateTime dateTime) {
        if (isNull(dateTime)) {
            return 0L;
        }
        return dateTime.atZone(systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDateTime fromMillis(long millis) {
        return ofEpochMilli(millis).atZone(systemDefault()).toLocalDateTime();
    }

    public static Date toSimpleDate(LocalDateTime dateTime) {
        if (isNull(dateTime)) {
            return new Date(0L);
        }
        return new Date(toMillis(dateTime));
    }

    public static LocalDateTime fromSimpleDate(Date date) {
        return ofEpochMilli(date.getTime()).atZone(systemDefault()).toLocalDateTime();
    }
}

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

package io.art.core.extension;

import lombok.experimental.*;
import io.art.core.exception.*;
import static java.util.Optional.*;
import static io.art.core.context.Context.*;
import java.text.*;
import java.util.*;

@UtilityClass
public class DateExtensions {
    public static Date parse(String template, String dateString) {
        return parse(template, dateString, contextConfiguration().getLocale());
    }

    public static Date parse(String template, String dateString, Locale locale) {
        try {
            return new SimpleDateFormat(template, locale).parse(dateString);
        } catch (ParseException throwable) {
            throw new InternalRuntimeException(throwable);
        }
    }

    public static Date tryParse(String dateTimeString, SimpleDateFormat format) {
        return tryParse(dateTimeString, format, null);
    }

    public static Date tryParse(String dateTimeString, SimpleDateFormat format, Date orElse) {
        try {
            return format.parse(dateTimeString);
        } catch (Throwable throwable) {
            return orElse;
        }
    }

    public static Optional<Date> parseSafety(String template, String dateString, Locale locale) {
        try {
            return of(new SimpleDateFormat(template, locale).parse(dateString));
        } catch (ParseException throwable) {
            throwable.printStackTrace();
            return empty();
        }
    }


    public static String format(String template, Date date) {
        return format(template, date, contextConfiguration().getLocale());
    }

    public static String tryFormat(Date dateTime, SimpleDateFormat format, String orElse) {
        try {
            return format.format(dateTime);
        } catch (Throwable throwable) {
            return orElse;
        }
    }

    public static String tryFormat(Date dateTime, SimpleDateFormat format) {
        return tryFormat(dateTime, format, null);
    }

    public static String format(String template, Date date, Locale locale) {
        return new SimpleDateFormat(template, locale).format(date);
    }
}

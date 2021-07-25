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

package io.art.core.parser;

import io.art.core.exception.*;
import lombok.experimental.*;
import static io.art.core.constants.CharacterConstants.NEW_LINE;
import static io.art.core.constants.CharacterConstants.SPACE;
import static io.art.core.constants.DurationConstants.*;
import static io.art.core.constants.Errors.*;
import static io.art.core.constants.RegExps.*;
import static io.art.core.constants.StringConstants.*;
import static java.lang.Character.*;
import static java.lang.Double.*;
import static java.lang.Long.*;
import static java.text.MessageFormat.*;
import static java.time.Duration.*;
import static java.util.concurrent.TimeUnit.*;
import java.time.*;
import java.util.concurrent.*;

@UtilityClass
public class DurationParser {
    public Duration parseDuration(String value) {
        value = unicodeTrim(value);
        String unitString = getUnits(value);
        String numberString = unicodeTrim(value.substring(0, value.length() - unitString.length()));

        TimeUnit units;

        if (numberString.length() == 0) {
            return null;
        }

        if (unitString.length() > 2 && !unitString.endsWith(SECONDS_LETTER))
            unitString = unitString + SECONDS_LETTER;

        switch (unitString) {
            case EMPTY_STRING:
            case MILLIS_LETTERS:
            case MILLIS_SHORT:
            case MILLIS_FULL:
                units = MILLISECONDS;
                break;
            case MICROS_LETTERS:
            case MICROS_SHORT:
            case MICROS_FULL:
                units = MICROSECONDS;
                break;
            case NANO_LETTERS:
            case NANO_SHORT:
            case NANO_FULL:
                units = NANOSECONDS;
                break;
            case DAYS_LETTER:
            case DAYS_FULL:
                units = DAYS;
                break;
            case HOURS_LETTER:
            case HOURS_FULL:
                units = HOURS;
                break;
            case SECONDS_LETTER:
            case SECONDS_FULL:
                units = SECONDS;
                break;
            case MINUTES_LETTER:
            case MINUTES_FULL:
                units = MINUTES;
                break;
            default:
                throw new ParseException(format(UNKNOWN_DURATION_TIME_UNITS, unitString));
        }

        if (numberString.matches(NUMBER_REGEXP)) {
            return ofNanos(units.toNanos(parseLong(numberString)));
        }

        long nanosInUnit = units.toNanos(1);
        return ofNanos((long) (parseDouble(numberString) * nanosInUnit));
    }

    private static String unicodeTrim(String value) {
        final int length = value.length();
        if (length == 0)
            return value;

        int start = 0;
        while (start < length) {
            char c = value.charAt(start);
            if (c == SPACE || c == NEW_LINE) {
                start += 1;
            } else {
                int cp = value.codePointAt(start);
                if (isWhitespace(cp))
                    start += charCount(cp);
                else
                    break;
            }
        }

        int end = length;
        while (end > start) {
            char character = value.charAt(end - 1);
            if (character == SPACE || character == NEW_LINE) {
                --end;
            } else {
                int codePoint;
                int delta;
                if (isLowSurrogate(character)) {
                    codePoint = value.codePointAt(end - 2);
                    delta = 2;
                } else {
                    codePoint = value.codePointAt(end - 1);
                    delta = 1;
                }
                if (isWhitespace(codePoint))
                    end -= delta;
                else
                    break;
            }
        }

        return value.substring(start, end);
    }

    private static String getUnits(String value) {
        int count = value.length() - 1;
        while (count >= 0) {
            char character = value.charAt(count);
            if (!isLetter(character))
                break;
            count -= 1;
        }
        return value.substring(count + 1);
    }
}

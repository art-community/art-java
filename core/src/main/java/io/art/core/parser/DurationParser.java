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

package io.art.core.parser;

import io.art.core.exception.*;
import lombok.experimental.*;
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

        TimeUnit units = null;

        if (numberString.length() == 0) {
            return null;
        }

        if (unitString.length() > 2 && !unitString.endsWith("s"))
            unitString = unitString + "s";

        switch (unitString) {
            case "":
            case "ms":
            case "millis":
            case "milliseconds":
                units = MILLISECONDS;
                break;
            case "us":
            case "micros":
            case "microseconds":
                units = MICROSECONDS;
                break;
            case "ns":
            case "nanos":
            case "nanoseconds":
                units = NANOSECONDS;
                break;
            case "d":
            case "days":
                units = DAYS;
                break;
            case "h":
            case "hours":
                units = HOURS;
                break;
            case "s":
            case "seconds":
                units = SECONDS;
                break;
            case "m":
            case "minutes":
                units = MINUTES;
                break;
            default:
                throw new ParserException(format("Unknown duration time units: {0}", unitString));
        }

        if (numberString.matches("[+-]?[0-9]+")) {
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
            if (c == ' ' || c == '\n') {
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
            if (character == ' ' || character == '\n') {
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
            char c = value.charAt(count);
            if (!isLetter(c))
                break;
            count -= 1;
        }
        return value.substring(count + 1);
    }
}

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

package io.art.core.extensions;

import io.art.core.collection.*;
import io.art.core.constants.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.StringConstants.*;
import static java.util.Arrays.*;
import static java.util.Objects.*;
import java.util.*;

@UtilityClass
public class StringExtensions {
    public static String toDelimitedString(Collection<?> collection, String delimiter, String prefix, String suffix) {
        if (isEmpty(collection)) {
            return StringConstants.EMPTY_STRING;
        }

        StringBuilder sb = new StringBuilder();
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static String toDelimitedString(Collection<?> collection, String delimiter) {
        return toDelimitedString(collection, delimiter, StringConstants.EMPTY_STRING, StringConstants.EMPTY_STRING);
    }

    public static String toDelimitedString(ImmutableCollection<?> collection, String delimiter, String prefix, String suffix) {
        if (isEmpty(collection)) {
            return StringConstants.EMPTY_STRING;
        }

        StringBuilder sb = new StringBuilder();
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static String toDelimitedString(ImmutableCollection<?> collection, String delimiter) {
        return toDelimitedString(collection, delimiter, StringConstants.EMPTY_STRING, StringConstants.EMPTY_STRING);
    }


    public static String toCommaDelimitedString(Collection<?> collection) {
        return toDelimitedString(collection, StringConstants.COMMA);
    }

    public static String toCommaDelimitedString(ImmutableCollection<?> collection) {
        return toDelimitedString(collection, StringConstants.COMMA);
    }

    public static String toCommaDelimitedString(Object[] array) {
        return toDelimitedString(asList(array), StringConstants.COMMA);
    }


    public static <T> String toString(T value, String ifNull) {
        if (isNull(value)) return ifNull;
        return value.toString();
    }

    public static String rewrite(String current, Map<String, String> rewritingMap) {
        if (isEmpty(rewritingMap)) return current;
        if (isEmpty(current)) return rewritingMap.getOrDefault(current, EMPTY_STRING);
        String newValue;
        return isNull((newValue = rewritingMap.get(current))) ? current : newValue;
    }

    public static <T> String emptyIfNull(T value) {
        return isNull(value) || value.equals(NULL_STRING) ? EMPTY_STRING : value.toString();
    }

    public static boolean isQuotedString(String string) {
        return string.length() >= 2 && ((string.startsWith(DOUBLE_QUOTES) && string.endsWith(DOUBLE_QUOTES)) || (string.startsWith(SINGLE_QUOTE) && string.endsWith(SINGLE_QUOTE)));
    }

    public static String unquote(String string) {
        if (isNull(string)) {
            return EMPTY_STRING;
        }
        return isQuotedString(string) ? string.substring(1, string.length() - 1) : string;
    }

    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String decapitalize(String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }
}

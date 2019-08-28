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

package ru.art.core.parser;

import lombok.experimental.*;
import static java.lang.Boolean.*;
import static java.lang.Double.*;
import static java.lang.Integer.*;
import static java.lang.Long.*;
import static ru.art.core.checker.CheckerForEmptiness.*;

@UtilityClass
public class PrimitiveParser {
    public static double parseOrElse(String str, double orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseDouble(str);
    }

    public static int parseOrElse(String str, int orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseInt(str);
    }

    public static long parseOrElse(String str, long orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseLong(str);
    }

    public static boolean parseOrElse(String str, boolean orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseBoolean(str);
    }

    public static double parseOrElse(String str, Double orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseDouble(str);
    }

    public static int parseOrElse(String str, Integer orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseInt(str);
    }

    public static long parseOrElse(String str, Long orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseLong(str);
    }

    public static boolean parseOrElse(String str, Boolean orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseBoolean(str);
    }


    public static double tryParseOrElse(String str, double orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseDouble(str);
        } catch (Throwable e) {
            return orElse;
        }
    }

    public static int tryParseOrElse(String str, int orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseInt(str);
        } catch (Throwable e) {
            return orElse;
        }
    }

    public static long tryParseOrElse(String str, long orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseLong(str);
        } catch (Throwable e) {
            return orElse;
        }
    }

    public static boolean tryParseOrElse(String str, boolean orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseBoolean(str);
        } catch (Throwable e) {
            return orElse;
        }
    }

    public static double tryParseOrElse(String str, Double orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseDouble(str);
        } catch (Throwable e) {
            return orElse;
        }
    }

    public static int tryParseOrElse(String str, Integer orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseInt(str);
        } catch (Throwable e) {
            return orElse;
        }
    }

    public static long tryParseOrElse(String str, Long orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseLong(str);
        } catch (Throwable e) {
            return orElse;
        }
    }

    public static boolean tryParseOrElse(String str, Boolean orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseBoolean(str);
        } catch (Throwable e) {
            return orElse;
        }
    }


    public static Boolean tryParseBoolean(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return parseBoolean(str);
        } catch (Throwable e) {
            return null;
        }
    }

    public static Double tryParseDouble(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return parseDouble(str);
        } catch (Throwable e) {
            return null;
        }
    }

    public static Integer tryParseInt(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return parseInt(str);
        } catch (Throwable e) {
            return null;
        }
    }

    public static Long tryParseLong(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return parseLong(str);
        } catch (Throwable e) {
            return null;
        }
    }
}

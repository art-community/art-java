package ru.adk.core.parser;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;

public interface PrimitiveParser {
    static double parseOrElse(String str, double orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseDouble(str);
    }

    static int parseOrElse(String str, int orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseInt(str);
    }

    static long parseOrElse(String str, long orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseLong(str);
    }

    static boolean parseOrElse(String str, boolean orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseBoolean(str);
    }

    static double parseOrElse(String str, Double orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseDouble(str);
    }

    static int parseOrElse(String str, Integer orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseInt(str);
    }

    static long parseOrElse(String str, Long orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseLong(str);
    }

    static boolean parseOrElse(String str, Boolean orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        return parseBoolean(str);
    }


    static double tryParseOrElse(String str, double orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseDouble(str);
        } catch (Exception e) {
            return orElse;
        }
    }

    static int tryParseOrElse(String str, int orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseInt(str);
        } catch (Exception e) {
            return orElse;
        }
    }

    static long tryParseOrElse(String str, long orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseLong(str);
        } catch (Exception e) {
            return orElse;
        }
    }

    static boolean tryParseOrElse(String str, boolean orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseBoolean(str);
        } catch (Exception e) {
            return orElse;
        }
    }

    static double tryParseOrElse(String str, Double orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseDouble(str);
        } catch (Exception e) {
            return orElse;
        }
    }

    static int tryParseOrElse(String str, Integer orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseInt(str);
        } catch (Exception e) {
            return orElse;
        }
    }

    static long tryParseOrElse(String str, Long orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseLong(str);
        } catch (Exception e) {
            return orElse;
        }
    }

    static boolean tryParseOrElse(String str, Boolean orElse) {
        if (isEmpty(str)) {
            return orElse;
        }
        try {
            return parseBoolean(str);
        } catch (Exception e) {
            return orElse;
        }
    }


    static Boolean tryParseBoolean(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return parseBoolean(str);
        } catch (Exception e) {
            return null;
        }
    }

    static Double tryParseDouble(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return parseDouble(str);
        } catch (Exception e) {
            return null;
        }
    }

    static Integer tryParseInt(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return parseInt(str);
        } catch (Exception e) {
            return null;
        }
    }

    static Long tryParseLong(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return parseLong(str);
        } catch (Exception e) {
            return null;
        }
    }
}

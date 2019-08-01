package ru.adk.core.checker;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import java.util.Collection;
import java.util.Map;

public interface CheckerForEmptiness {
    static <T> boolean isNotEmpty(T val) {
        return nonNull(val) && !val.toString().trim().equals(EMPTY_STRING);
    }

    static <T> boolean isEmpty(T val) {
        return isNull(val) || val.toString().trim().isEmpty();
    }

    static <T> boolean isEmpty(T[] content) {
        return isNull(content) || content.length == 0;
    }

    static boolean isEmpty(byte[] content) {
        return isNull(content) || content.length == 0;
    }

    static boolean isEmpty(short[] content) {
        return isNull(content) || content.length == 0;
    }

    static boolean isEmpty(int[] content) {
        return isNull(content) || content.length == 0;
    }

    static boolean isEmpty(long[] content) {
        return isNull(content) || content.length == 0;
    }

    static boolean isEmpty(float[] content) {
        return isNull(content) || content.length == 0;
    }

    static boolean isEmpty(double[] content) {
        return isNull(content) || content.length == 0;
    }

    static boolean isEmpty(boolean[] content) {
        return isNull(content) || content.length == 0;
    }

    static boolean isEmpty(char[] content) {
        return isNull(content) || content.length == 0;
    }

    static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }

    static boolean isEmpty(Collection<?> collection) {
        return isNull(collection) || collection.isEmpty();
    }

    static <T> T ifEmpty(T val, T ifEmpty) {
        return isEmpty(val) ? ifEmpty : val;
    }
}

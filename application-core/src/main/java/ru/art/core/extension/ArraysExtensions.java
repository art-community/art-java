package ru.art.core.extension;

import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;
import static java.util.Objects.isNull;

public interface ArraysExtensions {
    static <T> T[] concatArrays(T[] a1, T[] a2) {
        if (isNull(a1) || a1.length == 0) return a2;
        if (isNull(a2) || a2.length == 0) return a1;
        T[] res = copyOf(a1, a1.length + a2.length);
        arraycopy(a2, 0, res, a1.length, a2.length);
        return res;
    }
}

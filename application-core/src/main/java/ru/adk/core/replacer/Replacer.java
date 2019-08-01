package ru.adk.core.replacer;

import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;

public interface Replacer {
    static <T> T replaceWith(T current, T from, T to) {
        if (isEmpty(current)) {
            if (isEmpty(from)) return to;
            return current;
        }
        if (current.equals(from)) return to;
        return current;
    }
}

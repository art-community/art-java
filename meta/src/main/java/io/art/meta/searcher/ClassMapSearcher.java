package io.art.meta.searcher;

import lombok.experimental.*;
import static java.util.Objects.*;
import java.util.*;

@UtilityClass
public class ClassMapSearcher {
    public static <T> T searchByClass(Map<Class<?>, T> map, Class<?> searching) {
        T value = map.get(searching);
        if (nonNull(value)) return value;

        for (Class<?> typeInterface : searching.getInterfaces()) {
            value = searchByClass(map, typeInterface);
            if (nonNull(value)) {
                return value;
            }
        }

        Class<?> superclass = searching.getSuperclass();
        if (nonNull(superclass) && !Object.class.equals(superclass)) {
            value = searchByClass(map, superclass);
            if (nonNull(value)) {
                return value;
            }
        }

        return null;
    }
}

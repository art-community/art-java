package io.art.meta.searcher;

import lombok.experimental.*;
import static java.util.Objects.*;
import java.util.*;

@UtilityClass
public class ClassSearcher {
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

    public static boolean isAssignableFrom(Class<?> searching, String interfaceName) {
        if (searching.getName().equals(interfaceName)) return true;

        for (Class<?> typeInterface : searching.getInterfaces()) {
            return isAssignableFrom(typeInterface, interfaceName);
        }

        return false;
    }
}

package io.art.meta.type;

import java.lang.reflect.*;

public class TypeSubstitutor {
    public static Type substituteWildcard(WildcardType wildcardType) {
        Type[] lowerBounds = wildcardType.getLowerBounds();
        if (lowerBounds.length != 0) return lowerBounds[0];

        Type[] upperBounds = wildcardType.getUpperBounds();
        if (upperBounds.length != 0) return upperBounds[0];
        return Object.class;
    }
}

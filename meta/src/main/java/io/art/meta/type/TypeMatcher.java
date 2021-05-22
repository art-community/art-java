package io.art.meta.type;

import static io.art.meta.type.TypeInspector.*;
import static io.art.meta.type.TypeSubstitutor.*;
import java.lang.reflect.*;
import java.util.*;

public class TypeMatcher {
    public static boolean typeMatches(Type first, Type second) {
        if (first == second) return true;
        first = boxed(first);
        second = boxed(second);
        if (isWildcard(first) && isWildcard(second)) return Objects.equals(first, second);
        if (isWildcard(first)) {
            return typeMatches(substituteWildcard((WildcardType) first), second);
        }
        if (isWildcard(second)) {
            return typeMatches(first, substituteWildcard((WildcardType) second));
        }
        if (isClass(first)) return first.equals(second);
        if (isParametrized(first) && !isParametrized(second)) return false;
        if (isParametrized(first) && isParametrized(second)) {
            ParameterizedType parameterizedFirst = (ParameterizedType) first;
            ParameterizedType parameterizedSecond = (ParameterizedType) second;
            if (!Objects.equals(parameterizedFirst.getRawType(), parameterizedSecond.getRawType())) {
                return false;
            }
            Type[] firstArguments = parameterizedFirst.getActualTypeArguments();
            Type[] secondArguments = parameterizedSecond.getActualTypeArguments();
            if (firstArguments.length != secondArguments.length) return false;
            for (int index = 0; index < firstArguments.length; index++) {
                if (!typeMatches(firstArguments[index], secondArguments[index])) return false;
            }
            return true;
        }
        if (isGenericArray(first) && !isGenericArray(second)) return false;
        if (isGenericArray(first) && isGenericArray(second)) {
            GenericArrayType firstGenericArray = (GenericArrayType) first;
            GenericArrayType secondGenericArray = (GenericArrayType) second;
            return typeMatches(firstGenericArray.getGenericComponentType(), secondGenericArray.getGenericComponentType());
        }
        return false;
    }

    public static boolean typeMatches(ParameterizedType owner, Type first, Type second) {
        if (first == second) return true;
        first = extractGenericPropertyType(owner, boxed(first));
        second = extractGenericPropertyType(owner, boxed(second));
        if (isWildcard(first) && isWildcard(second)) return Objects.equals(first, second);
        if (isWildcard(first)) {
            return typeMatches(owner, substituteWildcard((WildcardType) first), second);
        }
        if (isWildcard(second)) {
            return typeMatches(owner, first, substituteWildcard((WildcardType) second));
        }
        if (isClass(first)) return first.equals(second);
        if (isParametrized(first) && !isParametrized(second)) return false;
        if (isParametrized(first) && isParametrized(second)) {
            ParameterizedType parameterizedFirst = (ParameterizedType) first;
            ParameterizedType parameterizedSecond = (ParameterizedType) second;
            if (!Objects.equals(parameterizedFirst.getRawType(), parameterizedSecond.getRawType())) {
                return false;
            }
            Type[] firstArguments = parameterizedFirst.getActualTypeArguments();
            Type[] secondArguments = parameterizedSecond.getActualTypeArguments();
            if (firstArguments.length != secondArguments.length) return false;
            for (int index = 0; index < firstArguments.length; index++) {
                if (!typeMatches(owner, firstArguments[index], secondArguments[index])) {
                    return false;
                }
            }
            return true;
        }
        if (isGenericArray(first) && !isGenericArray(second)) return false;
        if (isGenericArray(first) && isGenericArray(second)) {
            GenericArrayType firstGenericArray = (GenericArrayType) first;
            GenericArrayType secondGenericArray = (GenericArrayType) second;
            return typeMatches(owner, firstGenericArray.getGenericComponentType(), secondGenericArray.getGenericComponentType());
        }
        return false;
    }
}

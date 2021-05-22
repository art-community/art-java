package io.art.meta.selector;

import io.art.meta.exception.*;
import io.art.meta.model.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.meta.constants.MetaTypes.*;
import static io.art.meta.type.TypeInspector.*;
import static java.util.Objects.*;
import java.lang.reflect.*;
import java.time.*;
import java.util.*;

public class MetaTypeSelector {
    public static MetaType<?> select(Type type) {
        type = boxed(type);
        if (isClass(type)) {
            if (Short.class.equals(type)) return META_SHORT;
            if (Integer.class.equals(type)) return META_INTEGER;
            if (Long.class.equals(type)) return META_LONG;
            if (Boolean.class.equals(type)) return META_BOOLEAN;
            if (Double.class.equals(type)) return META_DOUBLE;
            if (Byte.class.equals(type)) return META_BYTE;
            if (Float.class.equals(type)) return META_FLOAT;
            if (String.class.equals(type)) return META_STRING;
            if (UUID.class.equals(type)) return META_UUID;
            if (LocalDateTime.class.equals(type)) return META_LOCAL_DATE_TIME;
            if (ZonedDateTime.class.equals(type)) return META_ZONED_DATE_TIME;
            if (Date.class.equals(type)) return META_DATE;
            if (Duration.class.equals(type)) return META_DURATION;
        }
        if (isParametrized(type)) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (isCollection(type) && isNull(actualTypeArguments) || isEmpty(actualTypeArguments)) {
                throw new MetaException("");
            }
            if (isList(type)) {
                MetaType<?> parameter = select(actualTypeArguments[0]);
                return metaList(parameter);
            }
        }
        throw new MetaException("");
    }
}

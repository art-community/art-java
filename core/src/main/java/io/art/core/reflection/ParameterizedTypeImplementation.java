package io.art.core.reflection;

import lombok.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.StringConstants.*;
import static java.util.Objects.*;
import java.lang.reflect.*;
import java.util.*;

@Getter
public class ParameterizedTypeImplementation implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Class<?> rawType;
    private final Type ownerType;

    private ParameterizedTypeImplementation(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
        this.actualTypeArguments = actualTypeArguments;
        this.rawType = rawType;
        this.ownerType = nonNull(ownerType) ? ownerType : rawType.getDeclaringClass();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof ParameterizedType)) {
            return false;
        }

        ParameterizedType that = (ParameterizedType) other;
        Type thatOwner = that.getOwnerType();
        Type thatRawType = that.getRawType();
        return Objects.equals(ownerType, thatOwner) && Objects.equals(rawType, thatRawType) && Arrays.equals(actualTypeArguments, that.getActualTypeArguments());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(actualTypeArguments) ^ Objects.hashCode(ownerType) ^ Objects.hashCode(rawType);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        baseToString(builder);

        if (isEmpty(this.actualTypeArguments)) {
            return builder.toString();
        }

        builder.append(LESS);
        boolean last = true;
        for (Type argument : this.actualTypeArguments) {
            if (Objects.isNull(argument)) continue;
            if (!last) {
                builder.append(COMMA).append(SPACE);
            }
            builder.append(argument.getTypeName());
            last = false;
        }

        builder.append(MORE);

        return builder.toString();
    }

    private void baseToString(StringBuilder builder) {
        if (isNull(this.ownerType)) {
            builder.append(this.rawType.getName());
            return;
        }

        if (this.ownerType instanceof Class) {
            builder.append(((Class<?>) this.ownerType).getName());
            return;
        }

        builder.append(this.ownerType.toString()).append(DOT);
        if (this.ownerType instanceof ParameterizedType) {
            builder.append(this.rawType.getName().replace(((ParameterizedType) this.ownerType).getRawType() + DOLLAR, EMPTY_STRING));
            return;
        }

        builder.append(this.rawType.getName());
    }

    public static ParameterizedTypeImplementation parameterizedType(Class<?> rawType, Type... actualTypeArguments) {
        return new ParameterizedTypeImplementation(rawType, actualTypeArguments, null);
    }
}

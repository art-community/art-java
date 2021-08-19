package io.art.meta.model;

import lombok.*;
import javax.annotation.*;
import java.lang.reflect.*;

@Getter
public abstract class TypeReference<T> implements Comparable<TypeReference<T>> {
    protected final Type type;

    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    @Override
    public int compareTo(@Nullable TypeReference<T> other) {
        return 0;
    }
}

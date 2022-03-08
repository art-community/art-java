package io.art.storage.filter.model;

import io.art.core.annotation.*;
import io.art.storage.filter.implementation.*;

@Public
public interface FilterByString<Type> {
    FilterRule<Type> startsWith(String pattern);

    FilterRule<Type> endsWith(String pattern);

    FilterRule<Type> contains(String pattern);
}

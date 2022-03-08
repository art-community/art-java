package io.art.storage.filter.model;

import io.art.core.annotation.*;
import io.art.storage.filter.implementation.*;

@Public
public interface FilterBySpaceUseStrings<Type> extends FilterBySpaceUseValues<Type, String> {
    FilterRule<Type> startsWith(String pattern);

    FilterRule<Type> endsWith(String pattern);

    FilterRule<Type> contains(String pattern);
}

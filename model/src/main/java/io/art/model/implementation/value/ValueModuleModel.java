package io.art.model.implementation.value;

import io.art.core.collection.*;
import lombok.*;
import java.lang.reflect.*;

@Getter
@RequiredArgsConstructor
public class ValueModuleModel {
    private final ImmutableSet<Type> customTypes;
}

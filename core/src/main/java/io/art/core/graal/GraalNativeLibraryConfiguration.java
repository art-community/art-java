package io.art.core.graal;

import io.art.core.collection.*;
import lombok.*;
import java.util.*;

@Getter
@Builder
public class GraalNativeLibraryConfiguration {
    private final String name;
    private final boolean builtin;
    private final Type type;
    private final Set<String> builtinSymbolPrefixes;
    private final GraalNativeLibraryLocation location;

    public enum Type {
        DYNAMIC,
        STATIC
    }
}

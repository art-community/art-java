package io.art.meta.extensions;

import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.constants.StringConstants.*;
import static java.util.stream.Collectors.*;
import java.util.function.*;

@UtilityClass
public class MetaClassExtensions {
    public static String joinMethods(MetaClass<?> owner, Predicate<MetaMethod<?>> predicate) {
        return owner.methods().stream()
                .filter(predicate)
                .map(MetaMethod::toString)
                .collect(joining(NEW_LINE + NEW_LINE));
    }
}

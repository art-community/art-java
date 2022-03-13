package io.art.tarantool.factory;

import io.art.meta.model.*;
import lombok.experimental.*;
import org.msgpack.value.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static org.msgpack.value.ValueFactory.*;

@UtilityClass
public class TarantoolNameFactory {
    public static ImmutableStringValue spaceName(MetaClass<?> type) {
        return newString(idByDash(type.definition().type()));
    }
}

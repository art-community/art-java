package io.art.tarantool.factory;

import io.art.meta.model.*;
import lombok.experimental.*;
import org.msgpack.value.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

@UtilityClass
public class TarantoolNameFactory {
    @SafeVarargs
    public static <ModelType> ImmutableStringValue indexName(MetaField<? extends MetaClass<ModelType>, ?>... fields) {
        return newString(stream(fields).map(MetaField::name).collect(joining()));
    }

    public static ImmutableStringValue indexName(List<MetaField<? extends MetaClass<?>, ?>> fields) {
        return newString(fields.stream().map(MetaField::name).collect(joining()));
    }

    public static ImmutableStringValue spaceName(MetaClass<?> type) {
        return newString(idByDash(type.definition().type()));
    }
}

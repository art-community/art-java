package io.art.tarantool.factory;

import io.art.meta.model.*;
import lombok.experimental.*;
import org.msgpack.value.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.msgpack.value.ValueFactory.*;

@UtilityClass
public class TarantoolIndexNameFactory {
    @SafeVarargs
    public static <ModelType> ImmutableStringValue indexName(MetaField<MetaClass<ModelType>, ?>... fields) {
        return newString(stream(fields).map(MetaField::name).collect(joining()));
    }
}

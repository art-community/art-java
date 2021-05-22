package io.art.meta.constants;

import io.art.core.collection.*;
import io.art.meta.model.*;
import static io.art.value.mapping.ArrayMapping.*;
import static io.art.value.mapping.PrimitiveMapping.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

public interface MetaTypes {
    MetaType<Short> META_SHORT = new MetaType<>(Short.class, toShort, fromShort);
    MetaType<Integer> META_INTEGER = new MetaType<>(Integer.class, toInt, fromInt);
    MetaType<Long> META_LONG = new MetaType<>(Long.class, toLong, fromLong);
    MetaType<Boolean> META_BOOLEAN = new MetaType<>(Boolean.class, toBool, fromBool);
    MetaType<Double> META_DOUBLE = new MetaType<>(Double.class, toDouble, fromDouble);
    MetaType<Byte> META_BYTE = new MetaType<>(Byte.class, toByte, fromByte);
    MetaType<Float> META_FLOAT = new MetaType<>(Float.class, toFloat, fromFloat);
    MetaType<String> META_STRING = new MetaType<>(String.class, toString, fromString);

    MetaType<UUID> META_UUID = new MetaType<>(UUID.class, toUuid, fromUuid);
    MetaType<LocalDateTime> META_LOCAL_DATE_TIME = new MetaType<>(LocalDateTime.class, toLocalDateTime, fromLocalDateTime);
    MetaType<ZonedDateTime> META_ZONED_DATE_TIME = new MetaType<>(ZonedDateTime.class, toZonedDateTime, fromZonedDateTime);
    MetaType<Date> META_date = new MetaType<>(Date.class, toDate, fromDate);
    MetaType<Duration> META_duration = new MetaType<>(Duration.class, toDuration, fromDuration);

    static MetaType<List<?>> metaList(MetaType<?> parameter) {
        return new MetaType<>(List.class, toMutableList(parameter.toModel()), fromArray(parameter.fromModel()));
    }

    static MetaType<Set<?>> metaSet(MetaType<?> parameter) {
        return new MetaType<>(Set.class, toMutableSet(parameter.toModel()), fromSet(parameter.fromModel()));
    }

    static MetaType<Queue<?>> metaQueue(MetaType<?> parameter) {
        return new MetaType<>(Queue.class, toMutableQueue(parameter.toModel()), fromQueue(parameter.fromModel()));
    }

    static MetaType<Deque<?>> metaDeque(MetaType<?> parameter) {
        return new MetaType<>(Deque.class, toMutableDeque(parameter.toModel()), fromDeque(parameter.fromModel()));
    }

    static MetaType<Collection<?>> metaCollection(MetaType<?> parameter) {
        return new MetaType<>(Collection.class, toMutableCollection(parameter.toModel()), fromCollection(parameter.fromModel()));
    }

    static MetaType<Stream<?>> metaStream(MetaType<?> parameter) {
        return new MetaType<>(Stream.class, toStream(parameter.toModel()), fromStream(parameter.fromModel()));
    }

    static MetaType<ImmutableArray<?>> metaImmutableArray(MetaType<?> parameter) {
        return new MetaType<>(ImmutableArray.class, toImmutableArray(parameter.toModel()), fromImmutableArray(parameter.fromModel()));
    }

    static MetaType<ImmutableSet<?>> metaImmutableSet(MetaType<?> parameter) {
        return new MetaType<>(ImmutableSet.class, toImmutableSet(parameter.toModel()), fromImmutableSet(parameter.fromModel()));
    }
}

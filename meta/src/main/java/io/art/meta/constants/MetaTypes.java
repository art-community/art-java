package io.art.meta.constants;

import io.art.meta.model.*;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import static io.art.meta.model.MetaType.*;
import static io.art.value.mapping.PrimitiveMapping.*;
import java.time.*;
import java.util.*;

public interface MetaTypes {
    MetaType<Object> META_OBJECT = metaType(Object.class, ValueToModelMapper.identity(), ValueFromModelMapper.identity());
    MetaType<Value> META_VALUE = metaType(Value.class, ValueToModelMapper.identity(), ValueFromModelMapper.identity());
    MetaType<Short> META_SHORT = metaType(Short.class, toShort, fromShort);
    MetaType<Integer> META_INTEGER = metaType(Integer.class, toInt, fromInt);
    MetaType<Long> META_LONG = metaType(Long.class, toLong, fromLong);
    MetaType<Boolean> META_BOOLEAN = metaType(Boolean.class, toBool, fromBool);
    MetaType<Double> META_DOUBLE = metaType(Double.class, toDouble, fromDouble);
    MetaType<Byte> META_BYTE = metaType(Byte.class, toByte, fromByte);
    MetaType<Float> META_FLOAT = metaType(Float.class, toFloat, fromFloat);
    MetaType<String> META_STRING = metaType(String.class, toString, fromString);
    MetaType<UUID> META_UUID = metaType(UUID.class, toUuid, fromUuid);
    MetaType<LocalDateTime> META_LOCAL_DATE_TIME = metaType(LocalDateTime.class, toLocalDateTime, fromLocalDateTime);
    MetaType<ZonedDateTime> META_ZONED_DATE_TIME = metaType(ZonedDateTime.class, toZonedDateTime, fromZonedDateTime);
    MetaType<Date> META_DATE = metaType(Date.class, toDate, fromDate);
    MetaType<Duration> META_DURATION = metaType(Duration.class, toDuration, fromDuration);
}

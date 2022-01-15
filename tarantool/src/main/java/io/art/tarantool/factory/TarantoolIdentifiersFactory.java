package io.art.tarantool.factory;

import io.art.meta.model.*;
import io.art.storage.*;
import io.art.tarantool.communication.*;
import lombok.experimental.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;

@UtilityClass
public class TarantoolIdentifiersFactory {
    public static String storageId(MetaClass<? extends Storage> storage) {
        return idByDot(storage.definition().type());
    }

    public static String spaceId(MetaClass<? extends TarantoolSpace<?>> space) {
        return idByDot(space.definition().type());
    }
}

package io.art.tarantool.factory;

import io.art.meta.model.*;
import io.art.storage.*;
import lombok.experimental.*;
import static io.art.core.constants.CharacterConstants.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;

@UtilityClass
public class TarantoolIdentifiersFactory {
    public static String storageId(MetaClass<? extends Storage> storage) {
        return asId(storage.definition().type(), DOT);
    }

    public static String spaceId(MetaClass<? extends Space<?, ?>> space) {
        return asId(space.definition().type(), DOT);
    }
}

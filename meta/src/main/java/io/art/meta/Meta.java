package io.art.meta;

import io.art.core.annotation.*;
import io.art.meta.exception.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static io.art.meta.module.MetaModule.*;
import static java.text.MessageFormat.*;
import java.util.*;

@ForUsing
@UtilityClass
public class Meta {
    public static <T> MetaClass<T> declaration(Class<T> type) {
        MetaClass<?> metaClass = metaModule().configuration().library().classes().get(type);
        if (Objects.isNull(metaClass)) {
            throw new MetaException(format(META_CLASS_FOR_CLASS_NOT_EXISTS, type));
        }
        return cast(metaClass);
    }

    public static <T extends MetaLibrary> T library() {
        return cast(metaModule().configuration().library());
    }
}

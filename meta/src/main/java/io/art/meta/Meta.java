package io.art.meta;

import io.art.core.annotation.*;
import io.art.meta.exception.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static io.art.meta.model.RuntimeMetaType.*;
import static io.art.meta.module.MetaModule.*;
import static java.text.MessageFormat.*;
import static java.util.Optional.*;
import java.lang.reflect.*;
import java.util.*;

@ForUsing
@UtilityClass
public class Meta {
    public static <T, M extends MetaClass<T>> M declaration(Class<T> type) {
        return cast(findDeclaration(type).orElseThrow(() -> new MetaException(format(META_CLASS_FOR_CLASS_NOT_EXISTS, type))));
    }

    public static <T> MetaType<T> definition() {
        TypeReference<T> typeReference = new TypeReference<>() {
        };
        return definition(typeReference.getType());
    }

    public static <T> MetaType<T> definition(Class<?> type) {
        return cast(findDeclaration(cast(type))
                .map(MetaClass::definition)
                .orElseGet(() -> defineMetaType(type)));
    }

    public static <T> MetaType<T> definition(Type type) {
        if (type instanceof Class) return definition((Class<?>) type);
        return defineMetaType(type);
    }

    public static <T, M extends MetaClass<T>> Optional<M> findDeclaration(Class<T> type) {
        return cast(ofNullable(metaModule().configuration().library().classes().get(type)));
    }

    public static <T extends MetaLibrary> T library() {
        return cast(metaModule().configuration().library());
    }
}

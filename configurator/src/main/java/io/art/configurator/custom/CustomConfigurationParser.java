package io.art.configurator.custom;

import io.art.core.exception.*;
import io.art.core.source.*;
import io.art.meta.*;
import io.art.meta.exception.*;
import io.art.meta.model.*;
import io.art.meta.schema.MetaCreatorTemplate.*;
import io.art.meta.transformer.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.extensions.ArrayExtensions.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static io.art.meta.constants.MetaConstants.MetaTypeExternalKind.*;
import static java.text.MessageFormat.*;

@UtilityClass
public class CustomConfigurationParser {
    public <T> T parse(Class<T> javaClass, ConfigurationSource source) {
        return parse(Meta.declaration(javaClass), source);
    }

    public <T> T parse(MetaClass<T> metaClass, ConfigurationSource source) {
        MetaCreatorInstance creator = metaClass.creator().validate(MetaException::new).instantiate();
        for (MetaProperty<?> property : creator.propertyMap().values()) {
            apply(source.getNested(property.name()), nested -> creator.putValue(property, parseValue(nested, property.type())));
        }
        return cast(creator.create());
    }

    private Object parseValue(NestedConfiguration configuration, MetaType<?> type) {
        MetaTransformer<?> transformer = type.inputTransformer();
        switch (type.externalKind()) {
            case MAP:
            case LAZY_MAP:
                if (type.parameters().get(0).externalKind() != STRING) {
                    throw new MetaException(format(MAP_WITH_NO_STRING_KEY, type));
                }
                return transformer.fromMap(configuration.asMap(nested -> parseValue(nested, type.parameters().get(1))).toMutable());
            case ARRAY:
            case LAZY_ARRAY:
                return transformer.fromArray(configuration.asArray(nested -> parseValue(nested, orElse(type.arrayComponentType(), () -> type.parameters().get(0)))).toMutable());
            case LAZY:
                return transformer.fromLazy(() -> parseValue(configuration, type.parameters().get(0)));
            case STRING:
                return transformer.fromString(configuration.asString());
            case LONG:
                return transformer.fromLong(configuration.asLong());
            case DOUBLE:
                return transformer.fromDouble(configuration.asDouble());
            case FLOAT:
                return transformer.fromFloat(configuration.asFloat());
            case INTEGER:
                return transformer.fromInteger(configuration.asInteger());
            case BOOLEAN:
                return transformer.fromBoolean(configuration.asBoolean());
            case CHARACTER:
                return transformer.fromCharacter(configuration.asCharacter());
            case SHORT:
                return transformer.fromShort(configuration.asShort());
            case BYTE:
                return transformer.fromByte(configuration.asByte());
            case BINARY:
                return transformer.fromByteArray(unbox(configuration.asByteArray().toArray(Byte[]::new)));
            case ENTITY:
                return parseEntity(configuration, type.declaration());
        }
        throw new ImpossibleSituationException();
    }

    private Object parseEntity(NestedConfiguration configuration, MetaClass<?> entityType) {
        MetaCreatorInstance creator = entityType.creator().validate(MetaException::new).instantiate();
        for (MetaProperty<?> property : creator.propertyMap().values()) {
            apply(configuration.getNested(property.name()), nested -> creator.putValue(property, parseValue(nested, property.type())));
        }
        return creator.create();
    }
}

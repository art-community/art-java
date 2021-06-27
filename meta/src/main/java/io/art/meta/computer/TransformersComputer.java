/*
 * ART
 *
 * Copyright 2019-2021 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.meta.computer;

import io.art.meta.exception.*;
import io.art.meta.model.*;
import io.art.meta.transformer.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static io.art.meta.transformer.ArrayTransformers.*;
import static io.art.meta.transformer.CollectionTransformers.*;
import static io.art.meta.transformer.ImmutableCollectionTransformers.*;
import static io.art.meta.transformer.ImmutableMapTransformers.*;
import static io.art.meta.transformer.MapTransformers.*;
import static io.art.meta.transformer.PrimitiveTransformers.*;
import static io.art.meta.transformer.ReactiveTransformers.*;
import static io.art.meta.transformer.SpecialTransformers.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;

@UtilityClass
public class TransformersComputer {
    public static MetaTransformer<?> computeInputTransformer(MetaType<?> type) {
        if (nonNull(type.inputTransformer())) return type.inputTransformer();
        switch (type.internalKind()) {
            case VOID:
                return VOID_TRANSFORMER;
            case STRING:
                return STRING_TRANSFORMER;
            case LONG:
                return LONG_TRANSFORMER;
            case DOUBLE:
                return DOUBLE_TRANSFORMER;
            case SHORT:
                return SHORT_TRANSFORMER;
            case FLOAT:
                return FLOAT_TRANSFORMER;
            case INTEGER:
                return INTEGER_TRANSFORMER;
            case BYTE:
                return BYTE_TRANSFORMER;
            case CHARACTER:
                return CHARACTER_TRANSFORMER;
            case BOOLEAN:
                return BOOLEAN_TRANSFORMER;
            case DATE:
                return DATE_TRANSFORMER;
            case LOCAL_DATE_TIME:
                return LOCAL_DATE_TIME_TRANSFORMER;
            case ZONED_DATE_TIME:
                return ZONED_DATE_TIME_TRANSFORMER;
            case DURATION:
                return DURATION_TRANSFORMER;
            case ARRAY:
                return arrayTransformer(cast(type.arrayFactory()), computeInputTransformer(type.arrayComponentType()));
            case LONG_ARRAY:
                return LONG_ARRAY_TRANSFORMER;
            case DOUBLE_ARRAY:
                return DOUBLE_ARRAY_TRANSFORMER;
            case FLOAT_ARRAY:
                return FLOAT_ARRAY_TRANSFORMER;
            case INTEGER_ARRAY:
                return INTEGER_ARRAY_TRANSFORMER;
            case BOOLEAN_ARRAY:
                return BOOLEAN_ARRAY_TRANSFORMER;
            case CHARACTER_ARRAY:
                return CHARACTER_ARRAY_TRANSFORMER;
            case SHORT_ARRAY:
                return SHORT_ARRAY_TRANSFORMER;
            case BYTE_ARRAY:
                return BYTE_ARRAY_TRANSFORMER;
            case COLLECTION:
                return collectionTransformer(computeInputTransformer(type.parameters().get(0)));
            case IMMUTABLE_COLLECTION:
                return immutableCollectionTransformer(computeInputTransformer(type.parameters().get(0)));
            case LIST:
                return listTransformer(computeInputTransformer(type.parameters().get(0)));
            case IMMUTABLE_ARRAY:
                return immutableArrayTransformer(computeInputTransformer(type.parameters().get(0)));
            case SET:
                return setTransformer(computeInputTransformer(type.parameters().get(0)));
            case IMMUTABLE_SET:
                return immutableSetTransformer(computeInputTransformer(type.parameters().get(0)));
            case QUEUE:
                return queueTransformer(computeInputTransformer(type.parameters().get(0)));
            case DEQUEUE:
                return dequeTransformer(computeInputTransformer(type.parameters().get(0)));
            case STREAM:
                return streamTransformer(computeInputTransformer(type.parameters().get(0)));
            case MAP:
                return mapTransformer(computeInputTransformer(type.parameters().get(0)), computeInputTransformer(type.parameters().get(1)));
            case IMMUTABLE_MAP:
                return immutableMapTransformer(computeInputTransformer(type.parameters().get(0)), computeInputTransformer(type.parameters().get(1)));
            case FLUX:
                return fluxTransformer(computeInputTransformer(type.parameters().get(0)));
            case MONO:
                return monoTransformer(computeInputTransformer(type.parameters().get(0)));
            case LAZY:
                return lazyTransformer(computeInputTransformer(type.parameters().get(0)));
            case OPTIONAL:
                return optionalTransformer(computeInputTransformer(type.parameters().get(0)));
            case SUPPLIER:
                return supplierTransformer(computeInputTransformer(type.parameters().get(0)));
            case ENUM:
                return enumTransformer(cast(type.enumFactory()));
            case INPUT_STREAM:
                return INPUT_STREAM_TRANSFORMER;
            case OUTPUT_STREAM:
                return OUTPUT_STREAM_TRANSFORMER;
            case NIO_BUFFER:
                return NIO_BUFFER_TRANSFORMER;
            case NETTY_BUFFER:
                return NETTY_BUFFER_TRANSFORMER;
        }
        throw new TransformationException(format(TRANSFORMER_NOT_FOUND, type));
    }

    public static MetaTransformer<?> computeOutputTransformer(MetaType<?> type) {
        if (nonNull(type.outputTransformer())) return type.outputTransformer();
        switch (type.externalKind()) {
            case LONG:
                return LONG_TRANSFORMER;
            case STRING:
                return STRING_TRANSFORMER;
            case DOUBLE:
                return DOUBLE_TRANSFORMER;
            case SHORT:
                return SHORT_TRANSFORMER;
            case FLOAT:
                return FLOAT_TRANSFORMER;
            case INTEGER:
                return INTEGER_TRANSFORMER;
            case BYTE:
                return BYTE_TRANSFORMER;
            case CHARACTER:
                return CHARACTER_TRANSFORMER;
            case BOOLEAN:
                return BOOLEAN_TRANSFORMER;
            case ARRAY:
                switch (type.internalKind()) {
                    case ARRAY:
                    case LONG_ARRAY:
                    case DOUBLE_ARRAY:
                    case FLOAT_ARRAY:
                    case INTEGER_ARRAY:
                    case BOOLEAN_ARRAY:
                    case CHARACTER_ARRAY:
                    case SHORT_ARRAY:
                    case BYTE_ARRAY:
                        return listTransformer(computeOutputTransformer(type.arrayComponentType()));
                    case COLLECTION:
                    case IMMUTABLE_COLLECTION:
                    case LIST:
                    case IMMUTABLE_ARRAY:
                    case SET:
                    case IMMUTABLE_SET:
                    case QUEUE:
                    case DEQUEUE:
                    case STREAM:
                    case FLUX:
                        return listTransformer(computeOutputTransformer(type.parameters().get(0)));
                }
            case MAP:
                return mapTransformer(computeOutputTransformer(type.parameters().get(0)), computeOutputTransformer(type.parameters().get(1)));
        }
        throw new TransformationException(format(TRANSFORMER_NOT_FOUND, type));
    }
}

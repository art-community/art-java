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
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static io.art.meta.constants.MetaConstants.*;
import static io.art.meta.constants.MetaConstants.MetaTypeExternalKind.*;
import static io.art.meta.transformer.ArrayTransformers.*;
import static io.art.meta.transformer.CollectionTransformers.*;
import static io.art.meta.transformer.ImmutableCollectionTransformers.*;
import static io.art.meta.transformer.ImmutableMapTransformers.*;
import static io.art.meta.transformer.MapTransformers.*;
import static io.art.meta.transformer.PrimitiveTransformers.*;
import static io.art.meta.transformer.ReactiveTransformers.*;
import static io.art.meta.transformer.SpecialTransformers.*;
import static java.text.MessageFormat.*;
import java.util.*;

@UtilityClass
public class TransformersComputer {
    public static MetaTransformer<?> computeInputTransformer(MetaType<?> type) {
        switch (type.kind()) {
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

    public static OutputTransformers computeOutputTransformers(MetaType<?> type) {
        Map<MetaTypeExternalKind, MetaTransformer<?>> transformers = map();
        switch (type.kind()) {
            case VOID:
                break;
            case STRING:
                transformers.put(STRING, STRING_TRANSFORMER);
                break;
            case LONG:
                transformers.put(STRING, STRING_TRANSFORMER);
            case DOUBLE:
                break;
            case SHORT:
                break;
            case FLOAT:
                break;
            case INTEGER:
                break;
            case BYTE:
                break;
            case CHARACTER:
                break;
            case BOOLEAN:
                break;
            case DATE:
                break;
            case LOCAL_DATE_TIME:
                break;
            case ZONED_DATE_TIME:
                break;
            case DURATION:
                break;
            case ARRAY:
                break;
            case LONG_ARRAY:
                transformers.put(ARRAY, LONG_ARRAY_TRANSFORMER);
                break;
            case DOUBLE_ARRAY:
                break;
            case FLOAT_ARRAY:
                break;
            case INTEGER_ARRAY:
                break;
            case BOOLEAN_ARRAY:
                break;
            case CHARACTER_ARRAY:
                break;
            case SHORT_ARRAY:
                break;
            case BYTE_ARRAY:
                break;
            case COLLECTION:
                break;
            case IMMUTABLE_COLLECTION:
                break;
            case LIST:
                break;
            case IMMUTABLE_ARRAY:
                break;
            case SET:
                break;
            case IMMUTABLE_SET:
                break;
            case QUEUE:
                break;
            case DEQUEUE:
                break;
            case STREAM:
                break;
            case MAP:
                break;
            case IMMUTABLE_MAP:
                break;
            case FLUX:
                break;
            case MONO:
                break;
            case LAZY:
                break;
            case OPTIONAL:
                break;
            case SUPPLIER:
                break;
            case ENUM:
                break;
            case INPUT_STREAM:
                break;
            case OUTPUT_STREAM:
                break;
            case NIO_BUFFER:
                break;
            case NETTY_BUFFER:
                break;
        }
        throw new TransformationException(format(TRANSFORMER_NOT_FOUND, type));
    }
}

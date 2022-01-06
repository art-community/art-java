/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.meta.model;

import io.art.core.collection.*;
import io.art.core.exception.*;
import io.art.meta.transformer.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.meta.transformer.ArrayTransformer.*;
import static io.art.meta.transformer.BooleanArrayTransformer.*;
import static io.art.meta.transformer.BooleanTransformer.*;
import static io.art.meta.transformer.ByteArrayTransformer.*;
import static io.art.meta.transformer.ByteTransformer.*;
import static io.art.meta.transformer.CharacterArrayTransformer.*;
import static io.art.meta.transformer.CharacterTransformer.*;
import static io.art.meta.transformer.CollectionTransformer.*;
import static io.art.meta.transformer.DateTransformer.*;
import static io.art.meta.transformer.DefaultTransformer.*;
import static io.art.meta.transformer.DequeueTransformer.*;
import static io.art.meta.transformer.DoubleArrayTransformer.*;
import static io.art.meta.transformer.DoubleTransformer.*;
import static io.art.meta.transformer.DurationTransformer.*;
import static io.art.meta.transformer.EnumTransformer.*;
import static io.art.meta.transformer.FloatArrayTransformer.*;
import static io.art.meta.transformer.FloatTransformer.*;
import static io.art.meta.transformer.FluxTransformer.*;
import static io.art.meta.transformer.ImmutableArrayTransformer.*;
import static io.art.meta.transformer.ImmutableCollectionTransformer.*;
import static io.art.meta.transformer.ImmutableMapTransformer.*;
import static io.art.meta.transformer.ImmutableSetTransformer.*;
import static io.art.meta.transformer.InputStreamTransformer.*;
import static io.art.meta.transformer.IntegerArrayTransformer.*;
import static io.art.meta.transformer.IntegerTransformer.*;
import static io.art.meta.transformer.LazyTransformer.*;
import static io.art.meta.transformer.ListTransformer.*;
import static io.art.meta.transformer.LocalDateTimeTransformer.*;
import static io.art.meta.transformer.LongArrayTransformer.*;
import static io.art.meta.transformer.LongTransformer.*;
import static io.art.meta.transformer.MapTransformer.*;
import static io.art.meta.transformer.MonoTransformer.*;
import static io.art.meta.transformer.NettyBufferTransformer.*;
import static io.art.meta.transformer.NioBufferTransformer.*;
import static io.art.meta.transformer.OptionalTransformer.*;
import static io.art.meta.transformer.OutputStreamTransformer.*;
import static io.art.meta.transformer.QueueTransformer.*;
import static io.art.meta.transformer.SetTransformer.*;
import static io.art.meta.transformer.ShortArrayTransformer.*;
import static io.art.meta.transformer.ShortTransformer.*;
import static io.art.meta.transformer.StreamTransformer.*;
import static io.art.meta.transformer.StringTransformer.*;
import static io.art.meta.transformer.SupplierTransformer.*;
import static io.art.meta.transformer.ZonedDateTimeTransformer.*;
import static java.util.Objects.*;

@UtilityClass
public class TransformersComputer {
    public static MetaTransformer<?> computeInputTransformer(MetaType<?> type) {
        if (nonNull(type.inputTransformer())) return type.inputTransformer();
        CustomTransformers custom = metaModule().configuration().getCustomTransformers().get(type.type());
        if (nonNull(custom)) return custom.getInput().apply(type);
        ImmutableArray<MetaType<?>> parameters = type.parameters();
        switch (type.internalKind()) {
            case MONO:
                MetaTransformer<?> parameterTransformer = computeInputTransformer(parameters.get(0));
                return monoTransformer(parameterTransformer);
            case FLUX:
                parameterTransformer = computeInputTransformer(parameters.get(0));
                return fluxTransformer(parameterTransformer);
            case LAZY:
                parameterTransformer = computeInputTransformer(parameters.get(0));
                return lazyTransformer(parameterTransformer);
            case OPTIONAL:
                parameterTransformer = computeInputTransformer(parameters.get(0));
                return optionalTransformer(parameterTransformer);
            case SUPPLIER:
                parameterTransformer = computeInputTransformer(parameters.get(0));
                return supplierTransformer(parameterTransformer);
            default:
                return computeSimpleTransformer(type);
        }
    }

    public static MetaTransformer<?> computeOutputTransformer(MetaType<?> type) {
        if (nonNull(type.outputTransformer())) return type.outputTransformer();
        CustomTransformers custom = metaModule().configuration().getCustomTransformers().get(type.type());
        if (nonNull(custom)) return custom.getOutput().apply(type);
        ImmutableArray<MetaType<?>> parameters = type.parameters();
        switch (type.internalKind()) {
            case MONO:
                MetaTransformer<?> parameterTransformer = computeOutputTransformer(parameters.get(0));
                return monoTransformer(parameterTransformer);
            case FLUX:
                parameterTransformer = computeOutputTransformer(parameters.get(0));
                return fluxTransformer(parameterTransformer);
            case LAZY:
                parameterTransformer = computeOutputTransformer(parameters.get(0));
                return lazyTransformer(parameterTransformer);
            case OPTIONAL:
                parameterTransformer = computeOutputTransformer(parameters.get(0));
                return optionalTransformer(parameterTransformer);
            case SUPPLIER:
                parameterTransformer = computeOutputTransformer(parameters.get(0));
                return supplierTransformer(parameterTransformer);
            default:
                return computeSimpleTransformer(type);
        }
    }

    private MetaTransformer<?> computeSimpleTransformer(MetaType<?> type) {
        switch (type.internalKind()) {
            case ENTITY:
            case VOID:
                return DEFAULT_TRANSFORMER;
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
                return arrayTransformer(cast(type.arrayFactory()));
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
                return COLLECTION_TRANSFORMER;
            case IMMUTABLE_COLLECTION:
                return IMMUTABLE_COLLECTION_TRANSFORMER;
            case LIST:
                return LIST_TRANSFORMER;
            case IMMUTABLE_ARRAY:
                return IMMUTABLE_ARRAY_TRANSFORMER;
            case SET:
                return SET_TRANSFORMER;
            case IMMUTABLE_SET:
                return IMMUTABLE_SET_TRANSFORMER;
            case QUEUE:
                return QUEUE_TRANSFORMER;
            case DEQUEUE:
                return DEQUEUE_TRANSFORMER;
            case STREAM:
                return STREAM_TRANSFORMER;
            case MAP:
                return MAP_TRANSFORMER;
            case IMMUTABLE_MAP:
                return IMMUTABLE_MAP_TRANSFORMER;
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
        throw new ImpossibleSituationException();
    }
}

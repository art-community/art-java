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

import io.art.meta.model.*;
import io.art.meta.transformer.*;
import lombok.experimental.*;

@UtilityClass
public class TransformersComputer {
    public static MetaTransformer<?> computeInputTransformer(MetaType<?> type) {
        switch (type.kind()) {
            case VOID:
                break;
            case STRING:
                break;
            case LONG:
                break;
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
            case DATE_TIME:
                break;
            case DURATION:
                break;
            case ARRAY:
                break;
            case LONG_ARRAY:
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
            case IMMUTABLE_LIST:
                break;
            case SET:
                break;
            case IMMUTABLE_SET:
                break;
            case QUEUE:
                break;
            case IMMUTABLE_QUEUE:
                break;
            case DEQUEUE:
                break;
            case IMMUTABLE_DEQUEUE:
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
            case CUSTOM:
                break;
        }
    }

    public static OutputTransformers computeOutputTransformers(MetaType<?> type) {

    }
}

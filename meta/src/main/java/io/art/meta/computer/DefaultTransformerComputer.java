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
import static io.art.meta.constants.MetaConstants.*;
import static io.art.meta.transformer.PrimitiveTransformers.*;

@UtilityClass
public class DefaultTransformerComputer {
    public static MetaTransformer<?> computeDefaultTransformer(MetaType<?> type) {
        switch (type.kind()) {
            case ENTITY:
                break;
            case ARRAY:
                break;
            case STRING:
                return STRING_TRANSFORMER;
            case LONG:
                break;
            case DOUBLE:
                break;
            case FLOAT:
                break;
            case INT:
                break;
            case BOOL:
                break;
            case BYTE:
                break;
            case BINARY:
                break;
            case LAZY:
                break;
            case OPTIONAL:
                break;
            case DATE:
                break;
            case DATE_TIME:
                break;
            case SUPPLIER:
                break;
            case STREAM:
                break;
            case FLUX:
                break;
            case MONO:
                break;
            case ENUM:
                break;
        }
        throw new MetaException(UNSUPPORTED_TYPE);
    }
}

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

package io.art.meta.validator;

import io.art.meta.model.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static java.text.MessageFormat.*;

@UtilityClass
public class MetaTypeValidator {
    public static ValidationResult validate(MetaType<?> type) {
        switch (type.internalKind()) {
            case VOID:
            case STRING:
            case LONG:
            case DOUBLE:
            case SHORT:
            case FLOAT:
            case INTEGER:
            case BYTE:
            case CHARACTER:
            case BOOLEAN:
            case DATE:
            case LOCAL_DATE_TIME:
            case ZONED_DATE_TIME:
            case DURATION:
            case LONG_ARRAY:
            case DOUBLE_ARRAY:
            case FLOAT_ARRAY:
            case INTEGER_ARRAY:
            case BOOLEAN_ARRAY:
            case CHARACTER_ARRAY:
            case SHORT_ARRAY:
            case BYTE_ARRAY:
            case ENUM:
            case INPUT_STREAM:
            case OUTPUT_STREAM:
            case NIO_BUFFER:
            case NETTY_BUFFER:
                break;
            case ARRAY:
                if (type.arrayComponentType().internalKind() == UNKOWN) {
                    return new ValidationResult(false, format("{0} is array with unknown component type", type));
                }
                break;
            case COLLECTION:
            case IMMUTABLE_COLLECTION:
            case LIST:
            case IMMUTABLE_ARRAY:
            case SET:
            case IMMUTABLE_SET:
            case QUEUE:
            case DEQUEUE:
            case STREAM:
                if (type.parameters().isEmpty()) {
                    return new ValidationResult(false, format("{0} is collection without parameters", type));
                }
                if (type.parameters().get(0).internalKind() == UNKOWN) {
                    return new ValidationResult(false, format("{0} is collection with unknown component type", type));
                }
                break;
            case MAP:
            case IMMUTABLE_MAP:
                if (type.parameters().size() != 2) {
                    return new ValidationResult(false, format("{0} is map without parameters", type));
                }
                if (type.parameters().get(0).internalKind() == UNKOWN) {
                    return new ValidationResult(false, format("{0} is map with unknown key parameter", type));
                }
                if (type.parameters().get(1).internalKind() == UNKOWN) {
                    return new ValidationResult(false, format("{0} is map with unknown value parameter", type));
                }
                break;
            case FLUX:
            case MONO:
                if (type.parameters().isEmpty()) {
                    return new ValidationResult(false, format("{0} is Mono without parameters", type));
                }
                if (type.parameters().get(0).internalKind() == UNKOWN) {
                    return new ValidationResult(false, format("{0} is Mono with unknown parameter", type));
                }
                break;
            case LAZY:
                if (type.parameters().isEmpty()) {
                    return new ValidationResult(false, format("{0} is Lazy without parameters", type));
                }
                if (type.parameters().get(0).internalKind() == UNKOWN) {
                    return new ValidationResult(false, format("{0} is Lazy with unknown parameter", type));
                }
                break;
            case OPTIONAL:
                if (type.parameters().isEmpty()) {
                    return new ValidationResult(false, format("{0} is Optional without parameters", type));
                }
                if (type.parameters().get(0).internalKind() == UNKOWN) {
                    return new ValidationResult(false, format("{0} is Optional with unknown parameter", type));
                }
                break;
            case SUPPLIER:
                if (type.parameters().isEmpty()) {
                    return new ValidationResult(false, format("{0} is Supplier without parameters", type));
                }
                if (type.parameters().get(0).internalKind() == UNKOWN) {
                    return new ValidationResult(false, format("{0} is Supplier with unknown parameter", type));
                }
                break;
        }
        return new ValidationResult(true, EMPTY_STRING);
    }

    @Getter
    @AllArgsConstructor
    public static class ValidationResult {
        private final boolean valid;
        private final String message;
    }
}

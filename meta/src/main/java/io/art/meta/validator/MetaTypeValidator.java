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
import static io.art.meta.constants.MetaConstants.Errors.*;
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
                if (type.arrayComponentType().internalKind() == UNKNOWN) {
                    return new ValidationResult(false, format(ARRAY_WITH_UNKNOWN_TYPE, type));
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
                    return new ValidationResult(false, format(COLLECTION_WITHOUT_PARAMETERS, type));
                }
                if (type.parameters().get(0).internalKind() == UNKNOWN) {
                    return new ValidationResult(false, format(COLLECTION_WITH_UNKNOWN_TYPE, type));
                }
                break;
            case MAP:
            case IMMUTABLE_MAP:
                if (type.parameters().size() != 2) {
                    return new ValidationResult(false, format(MAP_WITHOUT_PARAMETERS, type));
                }
                if (type.parameters().get(0).internalKind() == UNKNOWN) {
                    return new ValidationResult(false, format(MAP_WITH_UNKNOWN_KEY_TYPE, type));
                }
                if (type.parameters().get(1).internalKind() == UNKNOWN) {
                    return new ValidationResult(false, format(MAP_WITH_UNKNOWN_VALUE_TYPE, type));
                }
                break;
            case FLUX:
            case MONO:
                if (type.parameters().isEmpty()) {
                    return new ValidationResult(false, format(PUBLISHER_WITHOUT_PARAMETERS, type));
                }
                if (type.parameters().get(0).internalKind() == UNKNOWN) {
                    return new ValidationResult(false, format(PUBLISHER_WITH_UNKNOWN_PARAMETER, type));
                }
                break;
            case LAZY:
                if (type.parameters().isEmpty()) {
                    return new ValidationResult(false, format(LAZY_WITHOUT_PARAMETERS, type));
                }
                if (type.parameters().get(0).internalKind() == UNKNOWN) {
                    return new ValidationResult(false, format(LAZY_WITH_UNKNOWN_PARAMETER, type));
                }
                break;
            case OPTIONAL:
                if (type.parameters().isEmpty()) {
                    return new ValidationResult(false, format(OPTIONAL_WITHOUT_PARAMETERS, type));
                }
                if (type.parameters().get(0).internalKind() == UNKNOWN) {
                    return new ValidationResult(false, format(OPTION_WITH_UNKNOWN_PARAMETER, type));
                }
                break;
            case SUPPLIER:
                if (type.parameters().isEmpty()) {
                    return new ValidationResult(false, format(SUPPLIER_WITHOUT_PARAMETERS, type));
                }
                if (type.parameters().get(0).internalKind() == UNKNOWN) {
                    return new ValidationResult(false, format(SUPPLIER_WITH_UNKNOWN_PARAMETER, type));
                }
                break;
        }
        return new ValidationResult(true, format(UNKNOWN_TYPE, type));
    }

    @Getter
    @AllArgsConstructor
    public static class ValidationResult {
        private final boolean valid;
        private final String message;
    }
}

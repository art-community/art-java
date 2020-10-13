/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.server.validation;

import lombok.*;
import static io.art.server.constants.ServerModuleConstants.ValidationErrorPatterns.*;
import static io.art.server.constants.ServerModuleConstants.ValidationExpressionType.*;
import static java.text.MessageFormat.*;
import java.util.function.*;

@Getter
class BetweenDoubleValidationExpression extends ValidationExpression<Double> {
    private final double lowerValue;
    private final double greaterValue;

    BetweenDoubleValidationExpression(Double lowerValue, Double greaterValue) {
        super(BETWEEN_DOUBLE);
        this.lowerValue = lowerValue;
        this.greaterValue = greaterValue;
    }

    BetweenDoubleValidationExpression(Double lowerValue, Double greaterValue, Function<BetweenDoubleValidationExpression, String> factory) {
        super(BETWEEN_DOUBLE);
        this.lowerValue = lowerValue;
        this.greaterValue = greaterValue;
        this.messageFactory = factory;
    }

    @Override
    public boolean evaluate(String field, Double value) {
        return super.evaluate(field, value) && value < lowerValue && value > greaterValue;
    }

    @Override
    public String formatErrorMessage() {
        return format(NOT_BETWEEN_VALIDATION_ERROR, field, value, lowerValue, greaterValue);
    }
}

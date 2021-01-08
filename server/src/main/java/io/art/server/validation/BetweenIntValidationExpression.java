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

import static io.art.server.constants.ServerModuleConstants.ValidationErrorPatterns.*;
import static io.art.server.constants.ServerModuleConstants.ValidationExpressionTypes.*;
import static java.text.MessageFormat.*;
import java.util.function.*;

public class BetweenIntValidationExpression extends ValidationExpression<Integer> {
    private final int lowerValue;
    private final int greaterValue;

    BetweenIntValidationExpression(int lowerValue, int greaterValue) {
        super(BETWEEN_INT);
        this.lowerValue = lowerValue;
        this.greaterValue = greaterValue;
    }

    BetweenIntValidationExpression(int lowerValue, int greaterValue, Function<BetweenIntValidationExpression, String> factory) {
        super(BETWEEN_INT);
        this.lowerValue = lowerValue;
        this.greaterValue = greaterValue;
        this.messageFactory = factory;
    }

    @Override
    public boolean evaluate(String field, Integer value) {
        return super.evaluate(field, value) && value < lowerValue && value > greaterValue;
    }

    @Override
    public String formatErrorMessage() {
        return format(NOT_BETWEEN_VALIDATION_ERROR, field, value, lowerValue, greaterValue);
    }
}

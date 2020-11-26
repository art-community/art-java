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
import static io.art.core.caster.Caster.*;
import java.util.function.*;

@Getter
public abstract class ValidationExpression<T> {
    protected String field;
    protected T value;
    protected final String type;
    protected Function<? extends ValidationExpression<?>, String> messageFactory = ValidationExpression::formatErrorMessage;

    protected ValidationExpression(String type) {
        this.type = type;
    }

    public boolean evaluate(String field, T value) {
        this.field = field;
        this.value = value;
        return value != null;
    }

    public String getErrorMessage() {
        return messageFactory.apply(cast(this));
    }

    public abstract String formatErrorMessage();
}

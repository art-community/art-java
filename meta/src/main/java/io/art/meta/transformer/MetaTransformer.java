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

package io.art.meta.transformer;

import io.art.meta.exception.*;
import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.extensions.OptionalExtensions.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static java.text.MessageFormat.*;
import java.util.*;
import java.util.function.*;

public interface MetaTransformer<T> {
    default T transform(Object value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value));
    }

    default T transform(@SuppressWarnings(OPTIONAL_USED_AS_FIELD) Optional<?> value) {
        return unwrap(value.map(this::transform));
    }

    default T transform(Mono<?> value) {
        return transform(value.block());
    }

    default T transform(Supplier<?> value) {
        return transform(value.get());
    }
}

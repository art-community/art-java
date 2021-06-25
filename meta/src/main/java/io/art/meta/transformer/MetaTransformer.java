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

import io.art.core.exception.*;
import io.art.core.property.*;
import reactor.core.publisher.*;
import java.util.*;
import java.util.function.*;

public interface MetaTransformer<T> {
    default T transform(Object value) {
        throw new ImpossibleSituationException();
    }

    default T transform(LazyProperty<?> value) {
        return transform(value.get());
    }

    default T transform(Optional<?> value) {
        return transform(value.get());
    }

    default T transform(Mono<?> value) {
        return transform(value.block());
    }

    default T transform(Supplier<?> value) {
        return transform(value.get());
    }
}

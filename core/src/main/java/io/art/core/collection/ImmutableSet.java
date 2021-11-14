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

package io.art.core.collection;

import static io.art.core.caster.Caster.*;
import static java.util.Collections.*;
import java.util.*;
import java.util.stream.*;

public interface ImmutableSet<T> extends ImmutableCollection<T> {
    ImmutableSet<?> EMPTY = new ImmutableSetImplementation<>(emptySet());

    Collector<Object, ?, ImmutableSet<Object>> COLLECTOR = Collector.of(
            ImmutableSet::immutableSetBuilder,
            Builder::add,
            Builder::combine,
            Builder::build
    );

    Set<T> toMutable();

    static <T> ImmutableSet<T> emptyImmutableSet() {
        return cast(EMPTY);
    }

    static <T> Collector<T, T, ImmutableSet<T>> immutableSetCollector() {
        return cast(COLLECTOR);
    }

    static <T> Builder<T> immutableSetBuilder() {
        return new Builder<>();
    }

    class Builder<T> {
        private final com.google.common.collect.ImmutableSet.Builder<T> builder = com.google.common.collect.ImmutableSet.builder();

        public Builder<T> add(T element) {
            builder.add(element);
            return this;
        }

        @SafeVarargs
        public final Builder<T> add(T... elements) {
            builder.add(elements);
            return this;
        }

        public Builder<T> addAll(Iterable<? extends T> elements) {
            builder.addAll(elements);
            return this;
        }

        public Builder<T> addAll(Iterator<? extends T> elements) {
            builder.addAll(elements);
            return this;
        }

        private Builder<T> combine(Builder<T> builder) {
            this.builder.addAll(builder.build());
            return this;
        }

        public ImmutableSet<T> build() {
            return new ImmutableSetImplementation<>(builder.build());
        }
    }
}

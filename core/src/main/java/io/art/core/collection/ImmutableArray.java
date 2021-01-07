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

package io.art.core.collection;

import com.google.common.collect.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Collections.*;
import java.util.*;
import java.util.stream.*;

public interface ImmutableArray<T> extends ImmutableCollection<T> {
    ImmutableArray<?> EMPTY = new ImmutableArrayImplementation<>(emptyList());

    Collector<Object, ?, ImmutableArray<Object>> COLLECTOR = Collector.of(
            ImmutableArray::immutableArrayBuilder,
            ImmutableArray.Builder::add,
            ImmutableArray.Builder::combine,
            ImmutableArray.Builder::build
    );

    T get(int index);

    List<T> toMutable();

    int indexOf(Object object);

    int lastIndexOf(Object object);

    default ImmutableArray<T> reverse() {
        Builder<T> builder = immutableArrayBuilder();
        for (int index = size() - 1; index > 0; index--) {
            builder.add(get(index));
        }
        return builder.build();
    }

    static <T> ImmutableArray<T> emptyImmutableArray() {
        return cast(EMPTY);
    }

    static <T> ImmutableArray<T> immutableSortedArray(Iterable<T> elements, Comparator<T> comparator) {
        return new ImmutableArrayImplementation<>(Ordering.from(comparator).immutableSortedCopy(elements));
    }

    static <T> Collector<T, T, ImmutableArray<T>> immutableArrayCollector() {
        return cast(COLLECTOR);
    }

    static <T> Builder<T> immutableArrayBuilder() {
        return new Builder<>();
    }

    static <T> Builder<T> immutableArrayBuilder(int size) {
        return new Builder<>(size);
    }

    class Builder<T> {
        private final ImmutableList.Builder<T> builder;

        public Builder() {
            builder = ImmutableList.builder();
        }

        @SuppressWarnings(UNSTABLE_API_USAGE)
        public Builder(int size) {
            builder = ImmutableList.builderWithExpectedSize(size);
        }

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

        public int size() {
            return build().size();
        }

        public ImmutableArray<T> build() {
            return new ImmutableArrayImplementation<>(builder.build());
        }
    }
}

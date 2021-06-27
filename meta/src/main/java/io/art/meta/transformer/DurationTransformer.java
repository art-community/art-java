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

import lombok.*;
import static lombok.AccessLevel.*;
import java.time.*;

@NoArgsConstructor(access = PRIVATE)
public class DurationTransformer implements MetaTransformer<Duration> {
    @Override
    public String toString(Duration value) {
        return value.toString();
    }

    @Override
    public Duration fromString(String value) {
        return Duration.parse(value);
    }

    @Override
    public Duration fromLong(Long value) {
        return Duration.ofMillis(value);
    }

    @Override
    public Long toLong(Duration value) {
        return value.toMillis();
    }

    public static DurationTransformer DURATION_TRANSFORMER = new DurationTransformer();
}

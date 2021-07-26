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

import io.art.core.format.*;
import io.art.core.parser.*;
import lombok.*;
import static java.lang.Math.*;
import static lombok.AccessLevel.*;
import java.time.*;

@NoArgsConstructor(access = PRIVATE)
public class DurationTransformer implements MetaTransformer<Duration> {
    @Override
    public String toString(Duration value) {
        return DurationFormatter.format(value);
    }

    @Override
    public Duration fromString(String value) {
        return DurationParser.parseDuration(value);
    }

    @Override
    public Duration fromLong(Long value) {
        return Duration.ofMillis(value);
    }

    @Override
    public Long toLong(Duration value) {
        return value.toMillis();
    }

    @Override
    public Duration fromInteger(Integer value) {
        return Duration.ofMillis(value);
    }

    @Override
    public Integer toInteger(Duration value) {
        return toIntExact(value.toMillis());
    }

    public static DurationTransformer DURATION_TRANSFORMER = new DurationTransformer();
}

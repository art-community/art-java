/*
 * ART
 *
 * Copyright 2019-2022 ART
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
import static io.art.core.constants.DateTimeConstants.*;
import static io.art.core.extensions.DateTimeExtensions.*;
import static java.time.LocalDateTime.*;
import static java.time.ZoneId.*;
import static lombok.AccessLevel.*;
import java.time.*;

@NoArgsConstructor(access = PRIVATE)
public class LocalDateTimeTransformer implements MetaTransformer<LocalDateTime> {
    @Override
    public String toString(LocalDateTime value) {
        return TRANSPORTABLE_FORMATTER.format(ZonedDateTime.of(value, systemDefault()));
    }

    @Override
    public LocalDateTime fromString(String value) {
        return parse(value, TRANSPORTABLE_FORMATTER);
    }

    @Override
    public LocalDateTime fromLong(Long value) {
        return localFromMillis(value);
    }

    @Override
    public Long toLong(LocalDateTime value) {
        return toMillis(value);
    }

    public static LocalDateTimeTransformer LOCAL_DATE_TIME_TRANSFORMER = new LocalDateTimeTransformer();
}

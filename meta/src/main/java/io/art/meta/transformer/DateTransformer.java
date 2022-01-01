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
import static lombok.AccessLevel.*;
import java.util.*;

@NoArgsConstructor(access = PRIVATE)
public class DateTransformer implements MetaTransformer<Date> {
    @Override
    public String toString(Date value) {
        return DEFAULT_FORMATTER.format(localFromSimpleDate(value));
    }

    @Override
    public Date fromString(String value) {
        return toSimpleDate(parse(value, DEFAULT_FORMATTER));
    }

    @Override
    public Date fromLong(Long value) {
        return new Date(value);
    }

    @Override
    public Long toLong(Date value) {
        return value.getTime();
    }

    public static DateTransformer DATE_TRANSFORMER = new DateTransformer();
}

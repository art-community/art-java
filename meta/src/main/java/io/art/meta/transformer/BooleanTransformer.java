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

@NoArgsConstructor(access = PRIVATE)
public class BooleanTransformer implements MetaTransformer<Boolean> {
    @Override
    public String toString(Boolean value) {
        return value.toString();
    }

    @Override
    public Boolean toBoolean(Boolean value) {
        return value;
    }

    @Override
    public Boolean fromString(String value) {
        return Boolean.parseBoolean(value);
    }

    @Override
    public Boolean fromBoolean(Boolean value) {
        return value;
    }

    public static BooleanTransformer BOOLEAN_TRANSFORMER = new BooleanTransformer();
}

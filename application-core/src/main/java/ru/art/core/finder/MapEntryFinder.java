/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.core.finder;

import lombok.experimental.*;
import static java.util.Objects.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.factory.CollectionsFactory.*;
import java.util.*;

@UtilityClass
public class MapEntryFinder {
    public static <T> T find(Map<String, T> map, String key) {
        if (isEmpty(map) || isEmpty(key)) {
            return null;
        }
        Queue<String> sections = queueOf(key.split(ESCAPED_DOT));
        Map<String, ?> foundMap = map;
        Object value = null;
        String section;
        while ((section = sections.poll()) != null) {
            value = foundMap.get(section);
            if (isNull(value)) return null;
            if (!(value instanceof Map)) {
                if (sections.size() > 1) return null;
                return cast(value);
            }
            foundMap = cast(value);
        }
        return cast(value);
    }

    public static boolean hasPath(Map<String, ?> map, String key) {
        return nonNull(find(map, key));
    }
}

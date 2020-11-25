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

package ru.art.tarantool.model;

import lombok.*;
import java.util.*;

@Getter
@Builder(buildMethodName = "map", builderMethodName = "entityFieldsMapping")
public class TarantoolEntityFieldsMapping {
    private final Map<String, Integer> fieldsMapping;

    public int map(String fieldName) {
        return fieldsMapping.get(fieldName);
    }
}
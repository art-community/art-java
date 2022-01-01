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

package io.art.yaml.constants;

public interface YamlModuleConstants {
    interface Errors {
        String YAML_ARRAY_FIELD_EXCEPTION = "Yaml at field {0} has array, but type {1} is not compatible with it";
        String YAML_OBJECT_FIELD_EXCEPTION = "Yaml at field {0} has object, but type {1} is not compatible with it";
        String YAML_ARRAY_IN_ARRAY_EXCEPTION = "Yaml has array inside array, but type {0} is not compatible with it";
        String YAML_OBJECT_IN_ARRAY_EXCEPTION = "Yaml has object inside array, but type {0} is not compatible with it";
    }

}

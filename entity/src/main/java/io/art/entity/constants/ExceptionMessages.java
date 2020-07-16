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

package io.art.entity.constants;

public interface ExceptionMessages {
    String FROM_MODULE_MAPPER_IS_NULL = "FromModel mapper is null";
    String TO_MODULE_MAPPER_IS_NULL = "ToModel mapper is null";
    String MAPPER_IS_NULL = "Mapper mapper is null";
    String VALUE_TYPE_IS_NULL = "Value type is null";
    String PRIMITIVE_TYPE_IS_NULL = "Primitive type is null";
    String NOT_PRIMITIVE_TYPE = "Not primitive type: ''{0}''";
    String XML_TAG_IS_EMPTY = "Xml tag is empty";
    String COLLECTION_METHOD_NOT_IMPLEMENTED = "Collection method not implemented: {0}";
}

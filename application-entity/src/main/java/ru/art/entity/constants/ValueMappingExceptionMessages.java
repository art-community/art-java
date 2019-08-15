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

package ru.art.entity.constants;

public interface ValueMappingExceptionMessages {
    String FROM_MODULE_MAPPER_IS_NULL = "FromModel mapper is null";
    String TO_MODULE_MAPPER_IS_NULL = "ToModel mapper is null";
    String MAPPER_IS_NULL = "Mapper mapper is null";
    String VALUE_TYPE_IS_NULL = "Value type is null";
    String PRIMITIVE_TYPE_IS_NULL = "Primitive type is null";
    String PRIMITIVE_TYPE_IS_NULL_DURING_PARSING = "PrimitiveType is null during parsing from string";
    String NOT_PRIMITIVE_TYPE = "Not primitive type: ''{0}''";
    String NFL_COLLECTIONS_ELEMENTS = "Not collection elements type: ''{0}''";
    String XML_TAG_IS_UNFILLED = "Xml tag is unfilled";
    String REQUEST_LIST_ELEMENTS_TYPE_INVALID = "Trying to receive list of type: ''{0}'' but collection elements type is ''{1}''";
    String REQUEST_SET_ELEMENTS_TYPE_INVALID = "Trying to receive set of type: ''{0}'' but collection elements type is ''{1}''";
    String REQUEST_QUEUE_ELEMENTS_TYPE_INVALID = "Trying to receive queue of type: ''{0}'' but collection elements type is ''{1}''";
    String UNABLE_TO_PARSE_DATE = "Unable to parse date";
}

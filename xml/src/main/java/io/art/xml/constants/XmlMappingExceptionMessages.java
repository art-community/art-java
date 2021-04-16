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

package io.art.xml.constants;

public interface XmlMappingExceptionMessages {
    String INCORRECT_XML_STRUCTURE = "Incorrect XML structure";
    String XML_FILE_HAS_NOT_END_DOCUMENT_TAG = "XML file hasn't END_DOCUMENT tag";
    String SAME_INPUT_OUTPUT = "Forbidden to have a rules with same input or output. Incorrect rule: ''{0}''";
    String UNEXPECTED_PARSED_PART_IN_RULE = "Unexpected parsed part ''{0}'' in rule: ''{1}''";
}

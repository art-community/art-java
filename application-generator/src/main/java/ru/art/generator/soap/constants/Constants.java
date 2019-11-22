/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.generator.soap.constants;

public interface Constants {
    String CREATE_METHOD = ".create()";
    String VALUE = "value";
    String BUILD = "build";
    String ON_VALIDATING = "onValidating";
    String VALIDATOR_VARIABLE = "validator";
    String XML_MAPPER = "XmlMapper";

    interface ToXmlModelConstants {
        String XML_ENTITY_TO_MODEL_MAPPER_LAMBDA = "xmlEntity -> $T.builder()";
        String XML_ENTITY_TO_MODEL_MAPPER_LAMBDA_FOR_OPERATION = "xmlEntity -> $T.$N.map(xmlEntity)";
        String XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA = "parameters -> $T.xmlEntityBuilder()";
        String XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA_FOR_OPERATION = "parameters -> $T.$N.map(parameters)";
        String ADD_FIELD = ".$L($L)";
        String MAP_TO_MODEL = ".$L($T.$N.map(xmlEntity.find($S)))";
        String MAP_TO_XML = ".child($T.$N.map(parameters.get$L()))";
        String GET_VALUE_BY_TAG = "xmlEntity.getValueByTag($S)";
        String CHILD_GET_VALUE = "child.getValue()";
        String CREATE_TAG = ".tag($S)";
        String CREATE_PREFIX = ".prefix($S)";
        String CREATE_NAMESPACE = ".namespace($S)";
        String CREATE_NAMESPACE_FIELD = ".namespaceField($S, $S)";
        String CREATE_VALUE = ".value($T.valueOf(parameters.$L()))";
        String CREATE_CHILD = ".child()";
        String TO_MODEL = "ToModel";
        String FROM_MODEL = "FromModel";
        String MAP_LIST_TO_MODEL_FOR_COMPLEX_TYPE = ".$L(xmlEntity.getChildren($S).stream().map($T.$N::map).collect($T.toList()))";
        String MAP_LIST_TO_MODEL_FOR_SIMPLE_TYPE = ".$L(xmlEntity.getChildren($S).stream().map(child -> $L).collect($T.toList()))";
        String MAP_LIST_TO_XML_FOR_COMPLEX_TYPE = ".children(parameters.get$L().stream().map($T.$N::map).collect($T.toList()))";
        String MAP_LIST_TO_XML_FOR_SIMPLE_TYPE = ".children(parameters.get$L().stream().map(child -> $T.$N($S, $S, $S, $T.valueOf(child))).collect($T.toList()))";
        String VALIDATOR = "validator.validate($S, $L, $T.$L);";
        String VALIDATOR_RANGE = "validator.validate($S, $L, $T.$L($T.$L($S), $T.$L($S)));";
        String VALIDATOR_CONTAINS = "validator.validate($S, $L, $T.$L($T.$L($L)));";
    }

    interface SupportJavaType {
        String STRING = "string";
        String BOOLEAN = "boolean";
        String BYTE = "byte";
        String FLOAT = "float";
        String DOUBLE = "double";
        String DECIMAL = "decimal";
        String LONG = "long";
        String INT = "int";
        String INTEGER = "integer";
        String DATE_TIME = "dateTime";
        String TIME = "time";
        String DATE = "date";
        String BYTE_ARRAY = "base64Binary";
    }
}

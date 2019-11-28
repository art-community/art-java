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

package ru.art.generator.soap.factory;

import static ru.art.generator.soap.constants.Constants.SupportJavaType.BOOLEAN;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.BYTE;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.BYTE_ARRAY;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.DATE;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.DATE_TIME;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.DECIMAL;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.DOUBLE;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.FLOAT;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.INT;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.INTEGER;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.LONG;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.STRING;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.TIME;

import com.predic8.schema.Attribute;
import com.predic8.schema.Element;
import com.predic8.schema.TypeDefinition;
import com.predic8.schema.restriction.facet.EnumerationFacet;
import com.predic8.schema.restriction.facet.Facet;
import com.predic8.schema.restriction.facet.FractionDigits;
import com.predic8.schema.restriction.facet.LengthFacet;
import com.predic8.schema.restriction.facet.MaxExclusiveFacet;
import com.predic8.schema.restriction.facet.MaxInclusiveFacet;
import com.predic8.schema.restriction.facet.MaxLengthFacet;
import com.predic8.schema.restriction.facet.MinExclusiveFacet;
import com.predic8.schema.restriction.facet.MinInclusiveFacet;
import com.predic8.schema.restriction.facet.MinLengthFacet;
import com.predic8.schema.restriction.facet.PatternFacet;
import com.predic8.schema.restriction.facet.TotalDigitsFacet;
import com.predic8.schema.restriction.facet.WhiteSpaceFacet;
import groovy.xml.QName;
import java.util.Date;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import ru.art.generator.exception.NotFoundPrefixException;
import ru.art.generator.soap.model.Restriction;
import ru.art.generator.soap.model.Restriction.RestrictionBuilder;
import ru.art.generator.soap.model.RestrictionOperation;

@UtilityClass
public class TypeFactory {

    public static Class<? extends Object> getTypeByString(String type) {
        switch (type) {
            case STRING:
                return String.class;
            case BYTE:
                return Byte.class;
            case BOOLEAN:
                return Boolean.class;
            case FLOAT:
                return Float.class;
            case DOUBLE:
            case DECIMAL:
                return Double.class;
            case LONG:
                return Long.class;
            case INT:
            case INTEGER:
                return Integer.class;
            case DATE_TIME:
            case TIME:
            case DATE:
                return Date.class;
            case BYTE_ARRAY:
                return Byte[].class;
            default:
                return Object.class;
        }
    }

    public static String getTypeByElement(Element element) {
        if (Objects.isNull(element.getType())) {
            if (Objects.nonNull(element.getEmbeddedType()) ) {
                if (element.getEmbeddedType().getQname() != null) {
                    return element.getEmbeddedType().getQname().getLocalPart();
                }
            } else {
                return element.getType().getLocalPart();
            }
        }
        if (Objects.nonNull(element.getRef())) {
            return checkRefAndGetType(element);
        }
        return Object.class.getSimpleName();
    }

    public static String checkRefAndGetType(Element element) {
        String localPart = element.getRef().getLocalPart();
        if (isObject(getTypeByString(localPart))) {
            element = element.getSchema().getElement(element.getRef());
            return element.getType().getLocalPart();
        }
        return element.getRef().getLocalPart();
    }

    public static String getTypeByAttribute(Attribute attribute) {
        if (Objects.nonNull(attribute.getType())) {
            return attribute.getType().getLocalPart();
        } else if (Objects.nonNull(attribute.getRef())) {
                return attribute.getRef().getLocalPart();
        } else {
            return Object.class.getSimpleName();
        }
    }

    public static TypeDefinition getTypeDefinitionByAttribute(Attribute attribute) {
        QName ref = null;
        if (Objects.nonNull(attribute.getType())) {
            ref = attribute.getType();

        } else if (Objects.nonNull(attribute.getRef())) {
            ref = attribute.getRef();
        }
        return attribute.getSchema().getType(ref);
    }

    @SneakyThrows
    public static TypeDefinition getTypeDefinition(Element element) {
        if (Objects.isNull(element.getType())) {
            if (Objects.nonNull(element.getEmbeddedType())) {
                return element.getEmbeddedType();
            } else if (Objects.nonNull(element.getRef())) {
                QName ref = element.getRef();
                if (Objects.nonNull(ref.getNamespaceURI())) {
                    Element refElement = element.getSchema().getElement(ref);
                    element.setName(refElement.getName());
                    return getTypeDefinition(refElement);
                }
                if (Objects.isNull(ref.getPrefix()) || ref.getPrefix().isEmpty()) {
                    throw new NotFoundPrefixException("Not fount prefix for ref about elememt "
                            + element.getName());
                }
                String namespace = element.getSchema().getNamespace(ref.getPrefix()).toString();
                QName qName = new QName(namespace, ref.getLocalPart(), ref.getPrefix());
                element.setName(ref.getLocalPart());
                return element.getSchema().getType(qName);
            }
        }
        return element.getSchema().getType(element.getType());
    }

    public static String getNamespaceByPrefix(Element element, String prefix) {
        return element.getSchema().getNamespace(prefix).toString();
    }

    public static Restriction getRestrictionByFacet(Facet facet) {
        RestrictionBuilder builder = Restriction.builder();
        builder.value(facet.getValue());

        if (facet instanceof MinLengthFacet) {
            builder.operation(RestrictionOperation.MIN_LENGTH);
        } else if (facet instanceof FractionDigits) {
            builder.operation(RestrictionOperation.FRACTION_DIGITS);
        } else if (facet instanceof TotalDigitsFacet) {
            builder.operation(RestrictionOperation.TOTAL_DIGITS);
        } else if (facet instanceof EnumerationFacet) {
            builder.operation(RestrictionOperation.ENUMERATION);
        } else if (facet instanceof LengthFacet) {
            builder.operation(RestrictionOperation.LENGTH);
        } else if (facet instanceof MaxExclusiveFacet) {
            builder.operation(RestrictionOperation.MAX_EXCLUSIVE);
        } else if (facet instanceof PatternFacet) {
            builder.operation(RestrictionOperation.PATTERN);
        } else if (facet instanceof MaxLengthFacet) {
            builder.operation(RestrictionOperation.MAX_LENGTH);
        } else if (facet instanceof MinExclusiveFacet) {
            builder.operation(RestrictionOperation.MIN_EXCLUSIVE);
        } else if (facet instanceof MaxInclusiveFacet) {
            builder.operation(RestrictionOperation.MAX_INCLUSIVE);
        } else if (facet instanceof MinInclusiveFacet) {
            builder.operation(RestrictionOperation.MIN_INCLUSIVE);
        } else if (facet instanceof WhiteSpaceFacet) {
            builder.operation(RestrictionOperation.WHITESPACE);
        }
        return builder.build();
    }

    public static boolean isObject(Class classString) {
        return classString.getTypeName().equals(Object.class.getName());
    }
}

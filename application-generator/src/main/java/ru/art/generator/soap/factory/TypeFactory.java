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

import com.predic8.schema.Element;
import com.predic8.schema.TypeDefinition;
import com.predic8.schema.restriction.facet.*;
import groovy.xml.QName;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import ru.art.generator.exception.NotFoundPrefixException;
import ru.art.generator.soap.model.Restriction;
import ru.art.generator.soap.model.Restriction.RestrictionBuilder;
import ru.art.generator.soap.model.RestrictionOperation;

import java.util.Date;

import static ru.art.generator.soap.constants.Constants.SupportJavaType.*;

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
            default:
                return Object.class;
        }
    }

    public static String getTypeByElement(Element element) {
        if (element.getType() == null) {
            if (element.getEmbeddedType() != null) {
                if (element.getEmbeddedType().getQname() != null) {
                    return element.getEmbeddedType().getQname().getLocalPart();
                }
            } else if (element.getRef() != null) {
                return element.getRef().getLocalPart();
            }
        } else {
            return element.getType().getLocalPart();
        }
        return "Object";
    }

    @SneakyThrows
    public static TypeDefinition getTypeDefinition(Element element) {
        if (element.getType() == null) {
            if (element.getEmbeddedType() != null) {
                return element.getEmbeddedType();
            } else if (element.getRef() != null) {
                QName ref = element.getRef();
                if (ref.getNamespaceURI() != null) {
                    Element refElement = element.getSchema().getElement(ref);
                    element.setName(refElement.getName());
                    return getTypeDefinition(refElement);
                }
                if (ref.getPrefix() == null || ref.getPrefix().isEmpty()) {
                    throw new NotFoundPrefixException("Not fount prefix for ref about elememt "
                            + element.getName());
                }
                String namespace = element.getSchema().getNamespace(ref.getPrefix()).toString();
                QName qName = new QName(namespace, ref.getLocalPart(), ref.getPrefix());
                element.setName(ref.getLocalPart());
                return element.getSchema().getType(qName);
            }
        }
        String localPart = getTypeByElement(element);
        return element.getSchema().getType(localPart);
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

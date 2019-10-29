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

import com.predic8.schema.*;
import com.predic8.schema.restriction.*;
import com.predic8.wsdl.*;
import com.predic8.xml.util.*;
import lombok.experimental.*;
import ru.art.generator.soap.model.Field;
import ru.art.generator.soap.model.Field.*;
import static java.util.Objects.*;
import static ru.art.generator.soap.factory.TypeFactory.*;
import java.util.*;
import java.util.stream.*;

@UtilityClass
public class FieldFactory {

    private final static String NECESSARY = "1";
    private final static String UNBOUNDED = "unbounded";

    public static List<Field> createFieldsByMessage(String message, Definitions wsdlDefinitions) {
        List<Element> elementList = new ArrayList<>();
        wsdlDefinitions.getMessage(message).getParts().stream().map(Part::getElement).forEach(elementList::add);
        return elementList.stream().map(FieldFactory::createFieldByElement).collect(Collectors.toList());
    }

    private static Field createFieldByElement(Element element) {
        Class classString = getTypeByString(getTypeByElement(element));
        FieldBuilder fieldBuilder = isObject(classString) ? createComplexField(element) : createPrimitiveField(element);
        fieldBuilder.necessary(isNecessary(element));
        fieldBuilder.list(isList(element));
        return fieldBuilder.build();
    }

    private static FieldBuilder createPrimitiveField(Element element) {
        String localPart = element.getType().getLocalPart();
        return Field.builder()
                .name(element.getName())
                .typeName(localPart)
                .type(getTypeByString(localPart));
    }

    private static FieldBuilder createComplexField(Element element) {
        TypeDefinition type = getTypeDefinition(element);
        FieldBuilder field = null;
        if (type instanceof ComplexType) {
            field = createFieldByComplexType(element, (ComplexType) type);
        } else if (type instanceof SimpleType) {
            field = createFieldBySimpleType(element, (SimpleType) type);
        }
        return field;
    }

    private static FieldBuilder createFieldByComplexType(Element element, ComplexType complexType) {
        SchemaComponent model = complexType.getModel();
        if (model instanceof ComplexContent) {
            return createFieldByComplexContent(element, complexType, (ComplexContent) model);
        }
        return createFieldByModelGroup(element, complexType, (Sequence) model);
    }

    private static FieldBuilder createFieldByModelGroup(Element element, ComplexType complexType,
                                                        ModelGroup modelGroup) {
        String typeName = isNull(complexType) || isNull(complexType.getName()) ? element.getName() : complexType.getName();
        return Field.builder()
                .name(element.getName())
                .type(Object.class)
                .typeName(typeName)
                .prefix(element.getPrefix())
                .namespace(element.getNamespaceUri())
                .fieldsList(
                        modelGroup.getParticles().stream().map(schemaComponent -> (Element) schemaComponent)
                                .map(FieldFactory::createFieldByElement).collect(Collectors.toList())
                );
    }

    private static FieldBuilder createFieldByComplexContent(Element element, ComplexType complexType,
                                                            ComplexContent complexContent) {
        ModelGroup model = (ModelGroup) complexContent.getDerivation().getModel();
        FieldBuilder fieldByModelGroup = createFieldByModelGroup(element, complexType, model);
        PrefixedName basePN = complexContent.getDerivation().getBasePN();
        fieldByModelGroup.parentName(basePN.getLocalName());
        fieldByModelGroup.parentPrefix(basePN.getPrefix());
        fieldByModelGroup.parentNamespace(getNamespaceByPrefix(element, basePN.getPrefix()));
        return fieldByModelGroup;
    }

    private static FieldBuilder createFieldBySimpleType(Element element, SimpleType simpleType) {
        BaseRestriction restriction = simpleType.getRestriction();
        Class classString = getTypeByString(restriction.getBase().getLocalPart());
        return Field.builder()
                .name(element.getName())
                .prefix(element.getPrefix())
                .namespace(element.getNamespaceUri())
                .type(classString)
                .typeName(restriction.getBase().getLocalPart())
                .restrictionList(
                        simpleType.getRestriction().getFacets()
                                .stream().map(TypeFactory::getRestrictionByFacet)
                                .collect(Collectors.toList())
                );
    }

    private static boolean isNecessary(Element element) {
        return NECESSARY.equals(element.getMinOccurs());
    }

    private static boolean isList(Element element) {
        return UNBOUNDED.equals(element.getMaxOccurs());
    }

}

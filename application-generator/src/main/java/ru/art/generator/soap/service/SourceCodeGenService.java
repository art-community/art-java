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

package ru.art.generator.soap.service;

import com.squareup.javapoet.*;
import lombok.*;
import ru.art.entity.mapper.ValueFromModelMapper.*;
import ru.art.entity.mapper.ValueToModelMapper.*;
import ru.art.generator.soap.model.*;
import static com.squareup.javapoet.ClassName.*;
import static com.squareup.javapoet.TypeSpec.*;
import static javax.lang.model.element.Modifier.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.StringExtensions.*;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.*;
import static ru.art.generator.soap.constants.Constants.*;
import static ru.art.generator.soap.factory.CodeBlockFactory.*;
import static ru.art.generator.soap.factory.JavaFileFactory.*;
import static ru.art.generator.soap.model.SoapGenerationMode.*;
import java.util.*;

@RequiredArgsConstructor
public class SourceCodeGenService {

    private final String packageString;
    private final String absolutePathToSrcMainJava;

    private static String getValidatorMethodName(String type) {
        switch (type) {
            case "long":
                return "betweenLong";
            case "double":
                return "betweenDouble";
            case "int":
                return "betweenInt";
            default:
                return "betweenDouble";
        }
    }

    public void sourceGen(List<OperationSoapGen> operationSoapGenList, SoapGenerationMode soapGenerationMode) {
        for (OperationSoapGen operation : operationSoapGenList) {
            List<FieldSpec> fieldSpecList = new ArrayList<>();
            for (Field input : operation.getInput()) {
                if (SERVER.equals(soapGenerationMode)) {
                    createModelFromXmlEntity(input, packageString, absolutePathToSrcMainJava);
                    fieldSpecList.add(createFieldSpec(get(XmlEntityToModelMapper.class), XML_ENTITY_TO_MODEL_MAPPER_LAMBDA_FOR_OPERATION, input, TO_MODEL));
                    continue;
                }
                createXmlEntityFromModel(input, packageString, absolutePathToSrcMainJava);
                fieldSpecList.add(createFieldSpec(get(XmlEntityFromModelMapper.class), XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA_FOR_OPERATION, input, FROM_MODEL));
            }

            for (Field output : operation.getOutput()) {
                addFieldSpec(soapGenerationMode, fieldSpecList, output);
            }
            for (Field fault : operation.getFault()) {
                addFieldSpec(soapGenerationMode, fieldSpecList, fault);
            }

            TypeSpec specOperation = interfaceBuilder(firstLetterToUpperCase(operation.getName())).addModifiers(PUBLIC, STATIC)
                    .addFields(fieldSpecList)
                    .build();
            createJavaFile(packageString + ".operation", specOperation, absolutePathToSrcMainJava);

        }
        System.out.println("Mappers created successfully");
    }

    private void addFieldSpec(SoapGenerationMode soapGenerationMode, List<FieldSpec> fieldSpecList, Field output) {
        if (CLIENT.equals(soapGenerationMode)) {
            createModelFromXmlEntity(output, packageString, absolutePathToSrcMainJava);
            fieldSpecList.add(createFieldSpec(get(XmlEntityToModelMapper.class), XML_ENTITY_TO_MODEL_MAPPER_LAMBDA_FOR_OPERATION, output, TO_MODEL));
            return;
        }
        createXmlEntityFromModel(output, packageString, absolutePathToSrcMainJava);
        fieldSpecList.add(createFieldSpec(get(XmlEntityFromModelMapper.class), XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA_FOR_OPERATION, output, FROM_MODEL));
    }

    private FieldSpec createFieldSpec(ClassName classNameXmlEntity, String lambda, Field field, String postFixForCode) {
        ClassName classNameMapper = get(packageString + ".mapper." + field.getPrefix(), firstLetterToUpperCase(field.getTypeName()) + XML_MAPPER);
        ClassName classNameModel = get(packageString + ".model." + field.getPrefix(), firstLetterToUpperCase(field.getTypeName()));
        CodeBlock xmlEntityCodeBlock = CodeBlock.builder()
                .add(NEW_LINE + DOUBLE_TABULATION + lambda,
                        classNameMapper, firstLetterToLowerCase(field.getName() + postFixForCode))
                .build();
        return FieldSpec
                .builder(ParameterizedTypeName.get(classNameXmlEntity, classNameModel),
                        firstLetterToLowerCase(field.getName()) + postFixForCode, PUBLIC, STATIC, FINAL)
                .initializer(xmlEntityCodeBlock)
                .build();
    }


}

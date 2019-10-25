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
import static com.squareup.javapoet.TypeSpec.*;
import static javax.lang.model.element.Modifier.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.StringExtensions.*;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.*;
import static ru.art.generator.soap.constants.Constants.XML_MAPPER;
import static ru.art.generator.soap.factory.CodeBlockFactory.*;
import static ru.art.generator.soap.factory.JavaFileFactory.*;
import java.util.*;

@RequiredArgsConstructor
public class SourceCodeGenService {

    private final String packageString;

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
                String postFix;
                String lambda;
                ClassName className;
                if (SoapGenerationMode.SERVER.equals(soapGenerationMode)) {
                    postFix = TO_MODEL;
                    lambda = XML_ENTITY_TO_MODEL_MAPPER_LAMBDA_FOR_OPERATION;
                    className = ClassName.get(XmlEntityToModelMapper.class);
                    createModelFromXmlEntity(input, packageString);
                } else {
                    postFix = FROM_MODEL;
                    lambda = XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA_FOR_OPERATION;
                    className = ClassName.get(XmlEntityFromModelMapper.class);
                    createXmlEntityFromModel(input, packageString);
                }
                fieldSpecList.add(createFieldSpec(className, lambda, input, postFix));
            }
            for (Field output : operation.getOutput()) {
                String postFix;
                String lambda;
                ClassName className;
                if (SoapGenerationMode.CLIENT.equals(soapGenerationMode)) {
                    postFix = TO_MODEL;
                    lambda = XML_ENTITY_TO_MODEL_MAPPER_LAMBDA_FOR_OPERATION;
                    className = ClassName.get(XmlEntityToModelMapper.class);
                    createModelFromXmlEntity(output, packageString);
                } else {
                    postFix = FROM_MODEL;
                    lambda = XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA_FOR_OPERATION;
                    className = ClassName.get(XmlEntityFromModelMapper.class);
                    createXmlEntityFromModel(output, packageString);
                }
                fieldSpecList.add(createFieldSpec(className, lambda, output, postFix));
            }
            for (Field fault : operation.getFault()) {
                String postFix;
                String lambda;
                ClassName className;
                if (SoapGenerationMode.CLIENT.equals(soapGenerationMode)) {
                    postFix = TO_MODEL;
                    lambda = XML_ENTITY_TO_MODEL_MAPPER_LAMBDA_FOR_OPERATION;
                    className = ClassName.get(XmlEntityToModelMapper.class);
                    createModelFromXmlEntity(fault, packageString);
                } else {
                    postFix = FROM_MODEL;
                    lambda = XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA_FOR_OPERATION;
                    className = ClassName.get(XmlEntityFromModelMapper.class);
                    createXmlEntityFromModel(fault, packageString);
                }
                fieldSpecList.add(createFieldSpec(className, lambda, fault, postFix));
            }

            TypeSpec specOperation = interfaceBuilder(firstLetterToUpperCase(operation.getName())).addModifiers(PUBLIC, STATIC)
                    .addFields(fieldSpecList)
                    .build();
            createJavaFile(packageString + ".operation", specOperation);

        }
        System.out.println("Mappers created successfully");
    }

    private FieldSpec createFieldSpec(ClassName classNameXmlEntity, String lambda, Field field, String postFixForCode) {
        ClassName classNameMapper = ClassName
                .get(packageString + ".mapper." + field.getPrefix(), firstLetterToUpperCase(field.getTypeName()) + XML_MAPPER);
        ClassName classNameModel = ClassName
                .get(packageString + ".model." + field.getPrefix(), firstLetterToUpperCase(field.getTypeName()));
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

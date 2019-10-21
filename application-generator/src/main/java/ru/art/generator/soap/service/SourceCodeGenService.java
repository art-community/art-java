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
import static ru.art.generator.soap.constants.Constants.*;
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
            ClassName classNameXmlEntityToModelMapper = ClassName.get(XmlEntityToModelMapper.class);
            ClassName classNameXmlEntityFromModelMapper = ClassName.get(XmlEntityFromModelMapper.class);
            List<FieldSpec> fieldSpecList = new ArrayList<>();
            for (Field input : operation.getInput()) {
                createModelFromXmlEntity(input, packageString);
                String postFix;
                if (SoapGenerationMode.SERVER.equals(soapGenerationMode)) {
                    postFix = TO_MODEL;
                } else {
                    postFix = FROM_MODEL;
                }
                fieldSpecList.add(
                        createFieldSpec(classNameXmlEntityToModelMapper, XML_ENTITY_TO_MODEL_MAPPER_LAMBDA_FOR_OPERATION, input, XML_MAPPER, postFix)
                );
            }
            for (Field output : operation.getOutput()) {
                createXmlEntityFromModel(output, packageString);
                String postFix;
                if (SoapGenerationMode.SERVER.equals(soapGenerationMode)) {
                    postFix = FROM_MODEL;
                } else {
                    postFix = TO_MODEL;
                }
                fieldSpecList.add(
                        createFieldSpec(classNameXmlEntityFromModelMapper, XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA_FOR_OPERATION, output, XML_MAPPER, postFix)
                );
            }
            for (Field fault : operation.getFault()) {
                String postFix;
                if (SoapGenerationMode.SERVER.equals(soapGenerationMode)) {
                    postFix = FROM_MODEL;
                    createXmlEntityFromModel(fault, packageString);
                } else {
                    postFix = TO_MODEL;
                    createModelFromXmlEntity(fault, packageString);
                }
                fieldSpecList.add(
                        createFieldSpec(classNameXmlEntityFromModelMapper, XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA_FOR_OPERATION,
                                fault, XML_MAPPER, postFix)
                );
            }

            TypeSpec specOperation = interfaceBuilder(firstLetterToUpperCase(operation.getName())).addModifiers(PUBLIC, STATIC)
                    .addFields(fieldSpecList)
                    .build();
            createJavaFile(packageString + ".operation", specOperation);

        }
        System.out.println("Mappers created successfully");
    }

    private FieldSpec createFieldSpec(ClassName classNameXmlEntity, String lambda, Field field, String postFix, String postFixForCode) {
        ClassName classNameMapper = ClassName
                .get(packageString + ".mapper." + field.getPrefix(), firstLetterToUpperCase(field.getTypeName()) + postFix);
        ClassName classNameModel = ClassName
                .get(packageString + ".model." + field.getPrefix(), firstLetterToUpperCase(field.getTypeName()));
        CodeBlock xmlEntityFromModelCodeBlock = CodeBlock.builder()
                .add(NEW_LINE + DOUBLE_TABULATION + lambda,
                        classNameMapper, firstLetterToLowerCase(field.getName() + postFixForCode))
                .build();
        return FieldSpec
                .builder(ParameterizedTypeName.get(classNameXmlEntity, classNameModel),
                        firstLetterToLowerCase(field.getName()) + FROM_MODEL, PUBLIC, STATIC, FINAL)
                .initializer(xmlEntityFromModelCodeBlock)
                .build();
    }


}

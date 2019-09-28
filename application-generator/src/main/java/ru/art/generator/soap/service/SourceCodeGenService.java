package ru.art.generator.soap.service;

import static com.squareup.javapoet.TypeSpec.interfaceBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static ru.art.core.constants.StringConstants.DOUBLE_TABULATION;
import static ru.art.core.constants.StringConstants.NEW_LINE;
import static ru.art.core.extension.StringExtensions.firstLetterToLowerCase;
import static ru.art.core.extension.StringExtensions.firstLetterToUpperCase;
import static ru.art.generator.common.constants.Constants.XML_MAPPER;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.FROM_MODEL;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.TO_MODEL;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA_FOR_OPERATION;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.XML_ENTITY_TO_MODEL_MAPPER_LAMBDA_FOR_OPERATION;
import static ru.art.generator.soap.factory.CodeBlockFactory.createModelFromXmlEntity;
import static ru.art.generator.soap.factory.CodeBlockFactory.createXmlEntityFromModel;
import static ru.art.generator.soap.factory.JavaFileFactory.createJavaFile;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper.XmlEntityToModelMapper;
import ru.art.generator.soap.model.Field;
import ru.art.generator.soap.model.ModeGeneration;
import ru.art.generator.soap.model.OperationSoapGen;

@RequiredArgsConstructor
public class SourceCodeGenService {

  private final String packageString;

  public void sourceGen(List<OperationSoapGen> operationSoapGenList, ModeGeneration modeGeneration) {
    for (OperationSoapGen operation : operationSoapGenList) {
      ClassName classNameXmlEntityToModelMapper = ClassName.get(XmlEntityToModelMapper.class);
      ClassName classNameXmlEntityFromModelMapper = ClassName.get(XmlEntityFromModelMapper.class);
      List<FieldSpec> fieldSpecList = new ArrayList<>();
      for (Field input : operation.getInput()) {
        createModelFromXmlEntity(input, packageString);
        String postFix;
        if (ModeGeneration.SERVER.equals(modeGeneration)) {
          postFix = TO_MODEL;
        } else {
          postFix = FROM_MODEL;
        }
        fieldSpecList.add(
            createFieldSpec(classNameXmlEntityToModelMapper, XML_ENTITY_TO_MODEL_MAPPER_LAMBDA_FOR_OPERATION, input, XML_MAPPER, postFix)
        );
      }
      for(Field output : operation.getOutput()) {
        createXmlEntityFromModel(output, packageString);
        String postFix;
        if (ModeGeneration.SERVER.equals(modeGeneration)) {
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
        if (ModeGeneration.SERVER.equals(modeGeneration)) {
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


}

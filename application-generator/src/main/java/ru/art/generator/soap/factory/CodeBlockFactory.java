package ru.art.generator.soap.factory;

import static com.squareup.javapoet.CodeBlock.join;
import static com.squareup.javapoet.CodeBlock.of;
import static com.squareup.javapoet.TypeSpec.interfaceBuilder;
import static java.util.Objects.isNull;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static ru.art.core.constants.DateConstants.HH_MM_SS_24H;
import static ru.art.core.constants.DateConstants.YYYY_MM_DD_DASH;
import static ru.art.core.constants.DateConstants.YYYY_MM_DD_T_HH_MM_SS_SSSXXX;
import static ru.art.core.constants.StringConstants.NEW_LINE;
import static ru.art.core.extension.StringExtensions.firstLetterToLowerCase;
import static ru.art.core.extension.StringExtensions.firstLetterToUpperCase;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.generator.common.constants.Constants.XML_MAPPER;
import static ru.art.generator.mapper.constants.Constants.BUILD_METHOD;
import static ru.art.generator.mapper.constants.Constants.GET;
import static ru.art.generator.mapper.constants.Constants.IS;
import static ru.art.generator.mapper.constants.Constants.PathAndPackageConstants.DOT_MAPPER_DOT;
import static ru.art.generator.soap.constants.Constants.CREATE_METHOD;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.BOOLEAN;
import static ru.art.generator.soap.constants.Constants.SupportJavaType.BYTE;
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
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.ADD_FIELD;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.CREATE_CHILD;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.CREATE_NAMESPACE;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.CREATE_NAMESPACE_FIELD;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.CREATE_PREFIX;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.CREATE_TAG;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.CREATE_VALUE;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.FROM_MODEL;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.GET_VALUE_BY_TAG;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.MAP_LIST_TO_MODEL_FOR_COMPLEX_TYPE;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.MAP_LIST_TO_MODEL_FOR_SIMPLE_TYPE;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.MAP_LIST_TO_XML_FOR_COMPLEX_TYPE;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.MAP_LIST_TO_XML_FOR_SIMPLE_TYPE;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.MAP_TO_MODEL;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.MAP_TO_XML;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.TO_MODEL;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.XML_ENTITY_TO_MODEL_MAPPER_LAMBDA;
import static ru.art.generator.soap.factory.JavaFileFactory.createJavaFile;
import static ru.art.generator.soap.factory.JavaFileFactory.createJavaFileByField;
import static ru.art.generator.soap.factory.TypeFactory.isObject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import ru.art.core.constants.DateConstants;
import ru.art.core.constants.StringConstants;
import ru.art.core.extension.DateExtensions;
import ru.art.core.factory.CollectionsFactory;
import ru.art.entity.XmlEntity;
import ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper.XmlEntityToModelMapper;
import ru.art.generator.soap.model.Field;

@UtilityClass
public class CodeBlockFactory {

  private static Map<String, JavaFile> interfaceModelFromXmlEntity = CollectionsFactory.mapOf();
  private static Map<String, JavaFile> interfaceXmlEntityFromModel = CollectionsFactory.mapOf();
  private static Map<String, JavaFile> createdModels = CollectionsFactory.mapOf();

  private static String TABULATION = StringConstants.DOUBLE_TABULATION;

  public static CodeBlock createXmlEntityFromModel(Field inputField, String packageString) {

    CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();

    checkAndAddProperties(codeBlockBuilder, inputField);

    CodeBlock codeBlock = codeBlockBuilder.build();

    List<CodeBlock> codeBlocks = dynamicArrayOf(of(XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA, XmlEntity.class));
    codeBlocks.add(codeBlock);
    for (Field field : inputField.getFieldsList()) {
      if (!isObject(field.getType())) {
         codeBlocks.add(createPrimitiveXmlValue(field));
      } else {
        codeBlocks.add(createXmlEntityFromModel(field, packageString));
      }
    }
    removeDoubleTabulation();
    codeBlocks.add(of(TABULATION + CREATE_METHOD));
    ClassName className = getModel(inputField, packageString);
    ClassName classNameXmlEntityFromModelMapper = ClassName.get(XmlEntityFromModelMapper.class);
    FieldSpec fieldSpec = FieldSpec.builder(ParameterizedTypeName.get(classNameXmlEntityFromModelMapper, className),
        firstLetterToLowerCase(inputField.getName()) + FROM_MODEL, PUBLIC, STATIC, FINAL)
        .initializer(join(codeBlocks, NEW_LINE))
        .build();
    String mapperName = firstLetterToUpperCase(inputField.getTypeName()) + XML_MAPPER;
    checkAndAddTypeSpec(interfaceXmlEntityFromModel, mapperName, inputField, fieldSpec, packageString);
    ClassName mapperClassName = ClassName.get(packageString + ".mapper." + inputField.getPrefix(), mapperName);
    codeBlock = inputField.isList() ?
        CodeBlock.builder()
            .add(TABULATION + MAP_LIST_TO_XML_FOR_COMPLEX_TYPE,
                firstLetterToUpperCase(inputField.getName()), mapperClassName, fieldSpec.name, Collectors.class)
            .build()
        : CodeBlock.builder()
            .add(TABULATION + MAP_TO_XML, mapperClassName, fieldSpec.name,
                firstLetterToUpperCase(inputField.getName()))
            .build();
    return codeBlock;
  }


  public static CodeBlock createModelFromXmlEntity(Field inputField, String packageString) {
    CodeBlock codeBlock;

    if (isObject(inputField.getType())) {
      ClassName className = getModel(inputField, packageString);
      List<CodeBlock> codeBlocks = dynamicArrayOf(of(XML_ENTITY_TO_MODEL_MAPPER_LAMBDA, className));
      for (Field field : inputField.getFieldsList()) {
        codeBlocks.add(createModelFromXmlEntity(field, packageString));
      }
      codeBlocks.add(of(TABULATION + BUILD_METHOD));
      ClassName classNameXmlEntityToModelMapper = ClassName.get(XmlEntityToModelMapper.class);
      FieldSpec fieldSpec = FieldSpec.builder(ParameterizedTypeName.get(classNameXmlEntityToModelMapper, className),
          firstLetterToLowerCase(inputField.getName()) + TO_MODEL, PUBLIC, STATIC, FINAL)
          .initializer(join(codeBlocks, NEW_LINE))
          .build();
      String mapperName = firstLetterToUpperCase((inputField.getTypeName())) + XML_MAPPER;
      checkAndAddTypeSpec(interfaceModelFromXmlEntity, mapperName, inputField, fieldSpec, packageString);
      ClassName mapperClassName = ClassName.get(packageString + ".mapper." + inputField.getPrefix(),
          mapperName);
      codeBlock = inputField.isList()
          ? CodeBlock.builder()
          .add(
              TABULATION + MAP_LIST_TO_MODEL_FOR_COMPLEX_TYPE, firstLetterToLowerCase(inputField.getName()),
              inputField.getName(), mapperClassName, fieldSpec.name, Collectors.class
          ).build()
          : CodeBlock.builder()
              .add(
                  TABULATION + MAP_TO_MODEL, firstLetterToLowerCase(inputField.getName()), mapperClassName,
                  fieldSpec.name, inputField.getName()
              ).build();
    } else {
      codeBlock = inputField.isList()
          ? CodeBlock.builder()
          .add(
              TABULATION + MAP_LIST_TO_MODEL_FOR_SIMPLE_TYPE, firstLetterToLowerCase(inputField.getName()),
              inputField.getName(), getTypeFromString(inputField.getTypeName(), inputField.getName()),
              Collectors.class
          ).build()
          : CodeBlock.builder()
              .add(TABULATION + ADD_FIELD, firstLetterToLowerCase(inputField.getName()),
                  getTypeFromString(inputField.getTypeName(), inputField.getName()))
              .build();
    }
    return codeBlock;
  }

  private static String getMethodName(Field inputField) {
    return inputField.getType().equals(boolean.class) ?
        IS + firstLetterToUpperCase(inputField.getName())
        : GET + firstLetterToUpperCase(inputField.getName());
  }

  private static void checkAndAddTypeSpec(Map<String, JavaFile> map, String mapperName, Field field,
      FieldSpec fieldSpec, String packageString) {
    if (map.containsKey(field.getPrefix() + mapperName)) {
      return;
    }
    TypeSpec mapper = interfaceBuilder(firstLetterToUpperCase(mapperName))
        .addModifiers(PUBLIC, STATIC)
        .addField(fieldSpec)
        .build();
    map.put(field.getPrefix() + mapperName, createJavaFile(packageString + DOT_MAPPER_DOT + field.getPrefix(), mapper));
  }

  private static ClassName getModel(Field field, String packageString) {
    if (!createdModels.containsKey(field.getPrefix() + field.getName())) {
      createdModels.put(field.getPrefix() + field.getName(), createJavaFileByField(field, packageString));
    }
    return ClassName
        .get(packageString + ".model." + field.getPrefix(), firstLetterToUpperCase(field.getTypeName()));
  }

  private static void checkValueAndAddToCodeBlock(CodeBlock.Builder codeBlockBuilder, String pattern, Object... args) {
    for (Object arg : args) {
      if (isNull(arg)) {
        return;
      }
    }
    codeBlockBuilder.add(pattern, args);
  }

  private static void addDoubleTabulation() {
    TABULATION = TABULATION + "\t\t";
  }

  private static void removeDoubleTabulation() {
    if (TABULATION.length() == 2) {
      return;
    }
    TABULATION = TABULATION.substring(0, TABULATION.length() - 2);
  }

  private static CodeBlock createPrimitiveXmlValue(Field field) {
    CodeBlock codeBlock;
    if (field.isList()) {
      codeBlock = CodeBlock.builder()
          .add(TABULATION + MAP_LIST_TO_XML_FOR_SIMPLE_TYPE,
              firstLetterToUpperCase(field.getName()), XmlEntity.class, "createChild", field.getPrefix(),
              field.getNamespace(), field.getName(),
              String.class, Collectors.class)
          .build();
    } else {
      CodeBlock.Builder tempBuilder = CodeBlock.builder()
          .add(TABULATION + CREATE_CHILD + NEW_LINE);
      addDoubleTabulation();
      checkAndAddProperties(tempBuilder, field);
      tempBuilder
          .add(TABULATION + CREATE_VALUE + NEW_LINE, String.class, getMethodName(field));
      removeDoubleTabulation();
      codeBlock = tempBuilder
          .add(TABULATION + BUILD_METHOD + NEW_LINE)
          .build();
    }
    return codeBlock;
  }

  private static void checkAndAddProperties(CodeBlock.Builder codeBlockBuilder, Field inputField) {

    checkValueAndAddToCodeBlock(codeBlockBuilder, TABULATION + CREATE_TAG + NEW_LINE,
        inputField.getName());

    addDoubleTabulation();

    checkValueAndAddToCodeBlock(codeBlockBuilder, TABULATION + CREATE_PREFIX + NEW_LINE,
        inputField.getPrefix());
    checkValueAndAddToCodeBlock(codeBlockBuilder, TABULATION + CREATE_NAMESPACE + NEW_LINE,
        inputField.getNamespace());
    checkValueAndAddToCodeBlock(codeBlockBuilder, TABULATION + CREATE_NAMESPACE_FIELD + NEW_LINE,
        inputField.getPrefix(), inputField.getNamespace());

    removeDoubleTabulation();
  }

    private static CodeBlock getTypeFromString(String value, String nameParameter) {
        CodeBlock.Builder builder = CodeBlock.builder();
        switch (value) {
            case STRING:
                builder.add(GET_VALUE_BY_TAG, nameParameter);
                return builder.build();
            case BOOLEAN:
                builder.add("$T.parseBoolean(" + GET_VALUE_BY_TAG + ")", Boolean.class, nameParameter);
                return builder.build();
            case BYTE:
                builder.add("$T.parseByte(" + GET_VALUE_BY_TAG + ")", Byte.class, nameParameter);
                return builder.build();
            case FLOAT:
                builder.add("$T.parseFloat(" + GET_VALUE_BY_TAG + ")", Float.class, nameParameter);
                return builder.build();
            case DOUBLE:
                builder.add("$T.parseDouble(" + GET_VALUE_BY_TAG + ")", Double.class, nameParameter);
                return builder.build();
            case DECIMAL:
                builder.add("$T.parseDouble(" + GET_VALUE_BY_TAG + ")", Double.class, nameParameter);
                return builder.build();
            case LONG:
                builder.add("$T.parseLong(" + GET_VALUE_BY_TAG + ")", Long.class, nameParameter);
                return builder.build();
            case INT:
                builder.add("$T.parseInt(" + GET_VALUE_BY_TAG + ")", Integer.class, nameParameter);
                return builder.build();
            case INTEGER:
                builder.add("$T.parseInt(" + GET_VALUE_BY_TAG + ")", Integer.class, nameParameter);
                return builder.build();
            case DATE_TIME:
                builder.add("$T.parse($T.$N, " + GET_VALUE_BY_TAG + ")", DateExtensions.class, DateConstants.class,
                    YYYY_MM_DD_DASH, nameParameter);
                return builder.build();
            case TIME:
                builder.add("$T.parse($T.$N, " + GET_VALUE_BY_TAG + ")", DateExtensions.class, DateConstants.class,
                    HH_MM_SS_24H, nameParameter);
                return builder.build();
            case DATE:
                builder.add("$T.parse($T.$N, " + GET_VALUE_BY_TAG + ")", DateExtensions.class, DateConstants.class,
                    YYYY_MM_DD_T_HH_MM_SS_SSSXXX, nameParameter);
                return builder.build();
            default:
                builder.add(GET_VALUE_BY_TAG, nameParameter);
                return builder.build();
        }
    }
}

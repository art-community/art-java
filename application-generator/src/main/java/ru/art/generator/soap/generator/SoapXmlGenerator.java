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

package ru.art.generator.soap.generator;

import com.squareup.javapoet.*;
import lombok.Builder;
import lombok.Value;
import lombok.*;
import ru.art.core.constants.*;
import ru.art.core.extension.*;
import ru.art.entity.*;
import ru.art.entity.mapper.ValueFromModelMapper.*;
import ru.art.entity.mapper.ValueToModelMapper.*;
import ru.art.generator.mapper.exception.*;
import ru.art.generator.soap.constants.*;
import ru.art.service.validation.*;
import ru.art.xml.descriptor.*;
import static com.squareup.javapoet.CodeBlock.*;
import static com.squareup.javapoet.TypeSpec.*;
import static javax.lang.model.element.Modifier.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.DateConstants.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.generator.mapper.constants.Constants.*;
import static ru.art.generator.soap.constants.Constants.*;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.*;
import static ru.art.xml.module.XmlModule.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.*;
import java.util.stream.*;

public class SoapXmlGenerator {
    private final static String COMPLEX_TYPE = "complexType";
    private final static String PORT_TYPE = "portType";
    private final static String SEQUENCE = "sequence";
    private final static String TYPES = "types";
    private final static String ELEMENT = "element";
    private final static String PART = "part";
    private final static String SIMPLE_TYPE = "simpleType";
    private final static String RESTRICTION = "restriction";
    private final static String TYPE = "type";
    private final static String NAME = "name";
    private final static String BASE = "base";
    private final static String MIN_OCCURS = "minOccurs";
    private final static String MAX_OCCURS = "maxOccurs";
    private final static String NECESSARY = "1";
    private final static String VALUE = "value";
    private final static String UNBOUNDED = "unbounded";
    private final static String MESSAGE = "message";
    private final static String XML_MAPPER = "XmlMapper";
    private final static String ATTRIBUTE = "attribute";
    private final static String TARGET_NAMESPACE = "targetNamespace";
    private final static String REF = "ref";
    private final static String IMPORT = "import";
    private final static String SCHEMA_LOCATION = "schemaLocation";
    private final static String NAMESPACE = "namespace";
    private final static String HTTP = "http://";
    private final static String MIN_INCLUSIVE = "minInclusive";
    private final static String MAX_INCLUSIVE = "maxInclusive";
    private final static String ENUMERATION = "enumeration";
    private final static String EQUALS = "equals";
    private final static String GREATER = "greater";
    private final static String LESSER = "lesser";

    private static Map<String, String> namespaces;
    private static List<XmlEntity> allSchemas;
    private static HashMap<String, JavaFile> setJavaFiles;
    private static HashMap<String, JavaFile> interfaceModelFromXmlEntity;
    private static HashMap<String, JavaFile> interfaceXmlEntityFromModel;
    private static List<Field> listTypes;
    private static String packege = null;

    public static void generateXmlByWSDLFile(File wsdl, String packegePath, boolean isServer) throws IOException {
        System.out.println("Reading WSDL");
        XmlEntity entity = XmlEntityReader.readXml(wsdl);
        generateCodeByWsdl(entity, wsdl.getPath(), packegePath, isServer);
    }

    public static void generateXmlByWSDLUrl(URL url, String packegePath, boolean isServer) throws IOException {
        System.out.println("Reading WSDL");
        BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) url.getContent()));
        String wsdl = reader.lines().collect(Collectors.joining());
        XmlEntity entity = XmlEntityReader.readXml(xmlModule().getXmlInputFactory(), wsdl);
        generateCodeByWsdl(entity, url.toExternalForm(), packegePath, isServer);
    }

    private static void generateCodeByWsdl(XmlEntity entity, String path, String packegePath, boolean isServer) throws IOException {
        packege = packegePath;
        namespaces = entity.getNamespaces();
        setJavaFiles = new HashMap<>();
        interfaceModelFromXmlEntity = new HashMap<>();
        interfaceXmlEntityFromModel = new HashMap<>();
        System.out.println("Read WSDL successfully");
        System.out.println("Start process creating models");
        allSchemas = entity.getChildren(TYPES);
        checkImports(allSchemas, path);
        listTypes = new ArrayList<>();
        for (XmlEntity schema : allSchemas) {
            List<XmlEntity> collect = schema.getChildren().stream().filter(child -> child.getTag().equals(ELEMENT)).collect(Collectors.toList());
            for (XmlEntity element : collect) {
                listTypes.add(createField(element, schema));
            }
        }

        for (Field field : listTypes) {
            setJavaFiles.put(deletePrefix(field.getTypeName()), createJavaFileByField(field));
        }

        System.out.println("Models created successfully");
        System.out.println("Start process creating mappers");

        List<XmlEntity> messages = entity.getChildren().stream().filter(params -> params.getTag().equals(MESSAGE)).collect(Collectors.toList());
        List<XmlEntity> operations = entity.getChildren(PORT_TYPE);

        for (XmlEntity operationWsdl : operations) {
            Operation operation = getOperationByMessage(messages, operationWsdl.getChildren(), operationWsdl.getAttributes().get(NAME), isServer);

            ClassName classNameXmlEntityToModelMapper = ClassName.get(XmlEntityToModelMapper.class);
            ClassName classNameXmlEntityFromModelMapper = ClassName.get(XmlEntityFromModelMapper.class);
            ClassName classNameInputXMLMapper = ClassName.get(packege + ".mapper." + operation.getInput().getPrefix(), getNameUpperCase(deletePrefix(operation.getInput().getTypeName())) + XML_MAPPER);
            ClassName classNameOutputXMLMapper = ClassName.get(packege + ".mapper." + operation.getOutput().getPrefix(), getNameUpperCase(deletePrefix(operation.getOutput().getTypeName())) + XML_MAPPER);
            ClassName classNameInput = ClassName.get(packege + ".model." + operation.getInput().getPrefix(), getNameUpperCase(deletePrefix(operation.getInput().getTypeName())));
            ClassName classNameOutput = ClassName.get(packege + ".model." + operation.getOutput().getPrefix(), getNameUpperCase(deletePrefix(operation.getOutput().getTypeName())));
            getModelFromXmlEntity(operation.getInput());
            getXmlEntityFromModel(operation.getOutput());
            CodeBlock xmlEntityToModelMapperCodeBlock = CodeBlock.builder()
                    .add(NEW_LINE + DOUBLE_TABULATION + XML_ENTITY_TO_MODEL_MAPPER_LAMBDA_FOR_OPERATION, classNameInputXMLMapper, getNameLowerCase(deletePrefix(operation.getInput().getName()) + TO_MODEL))
                    .build();
            CodeBlock xmlEntityFromModelCodeBlock = CodeBlock.builder()
                    .add(NEW_LINE + DOUBLE_TABULATION + XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA_FOR_OPERATION, classNameOutputXMLMapper, getNameLowerCase(deletePrefix(operation.getOutput().getName()) + FROM_MODEL))
                    .build();
            List<FieldSpec> listFaultMapper = new ArrayList<>();
            for (Field fault : operation.getFault()) {
                String postFix;
                if (isServer) {
                    postFix = XML_MAPPER;
                    getXmlEntityFromModel(fault);
                } else {
                    postFix = TO_MODEL;
                    getModelFromXmlEntity(fault);
                }
                ClassName classNameOutputFualtXMLMapper = ClassName.get(packege + ".mapper." + fault.getPrefix(), getNameUpperCase(deletePrefix(fault.getTypeName())) + postFix);
                ClassName classNameOutputFualt = ClassName.get(packege + ".model." + fault.getPrefix(), getNameUpperCase(deletePrefix(fault.getTypeName())));
                String name = listTypes.stream().filter(type -> type.getTypeName().equals(fault.getTypeName())).findFirst().get().getName();
                FieldSpec fieldSpecOutput = FieldSpec
                        .builder(ParameterizedTypeName.get(classNameXmlEntityFromModelMapper, classNameOutputFualt), getNameLowerCase(fault.getName()) + FROM_MODEL, PUBLIC, STATIC, FINAL)
                        .initializer(CodeBlock.builder().add(NEW_LINE + DOUBLE_TABULATION + XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA_FOR_OPERATION,
                                classNameOutputFualtXMLMapper, getNameLowerCase(name + FROM_MODEL)).build()).build();
                listFaultMapper.add(fieldSpecOutput);
            }
            FieldSpec fieldSpecInput = FieldSpec
                    .builder(ParameterizedTypeName.get(classNameXmlEntityToModelMapper, classNameInput), getNameLowerCase(operation.getInput().getName()) + TO_MODEL, PUBLIC, STATIC, FINAL)
                    .initializer(xmlEntityToModelMapperCodeBlock)
                    .build();
            FieldSpec fieldSpecOutput = FieldSpec
                    .builder(ParameterizedTypeName.get(classNameXmlEntityFromModelMapper, classNameOutput), getNameLowerCase(operation.getOutput().getName()) + FROM_MODEL, PUBLIC, STATIC, FINAL)
                    .initializer(xmlEntityFromModelCodeBlock)
                    .build();
            TypeSpec specOperation = interfaceBuilder(getNameUpperCase(operation.getName())).addModifiers(PUBLIC, STATIC)
                    .addField(fieldSpecInput)
                    .addField(fieldSpecOutput)
                    .addFields(listFaultMapper)
                    .build();
            createJavaFile(packege + ".operation", specOperation);
        }
        System.out.println("Mappers created successfully");
    }

    private static void checkImports(List<XmlEntity> allSchemas, String path) throws IOException {
        path = path.replaceAll("\\w+.wsdl", "");
        List<XmlEntity> removeList = new ArrayList<>();
        List<XmlEntity> addList = new ArrayList<>();
        for (XmlEntity schema : allSchemas) {
            Map<String, List<String>> importMap = new HashMap<>();
            for (XmlEntity childSchema : schema.getChildren()) {
                if (IMPORT.equals(childSchema.getTag()) && !removeList.contains(schema)) {
                    removeList.add(schema);
                }
                Map<String, String> attributes = childSchema.getAttributes();
                if (importMap.containsKey(attributes.get(NAMESPACE))) {
                    importMap.get(attributes.get(NAMESPACE)).add(attributes.get(SCHEMA_LOCATION));
                } else {
                    List listParameters = new ArrayList<>();
                    listParameters.add(attributes.get(SCHEMA_LOCATION));
                    if (isEmpty(attributes.get(NAMESPACE))) break;
                    importMap.put(attributes.get(NAMESPACE), listParameters);
                }
            }
            if (isEmpty(removeList)) break;
            if (isEmpty(importMap)) break;
            for (String key : importMap.keySet()) {
                XmlEntity newSchema = null;
                for (String schemaLocation : importMap.get(key)) {
                    XmlEntity entity;
                    if (!schemaLocation.contains(HTTP)) {
                        File xsd = new File(path + "\\" + schemaLocation);
                        entity = XmlEntityReader.readXml(xsd);
                    } else {
                        BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) new URL(schemaLocation).getContent()));
                        String wsdl = reader.lines().collect(Collectors.joining());
                        entity = XmlEntityReader.readXml(xmlModule().getXmlInputFactory(), wsdl);

                    }
                    String xml = XmlEntityWriter.writeXml(xmlModule().getXmlOutputFactory(), entity);
                    entity = XmlEntityReader.readXml(xmlModule().getXmlInputFactory(), xml.replaceAll("tns", getPrefixByNamespace(entity.getAttributes().get(TARGET_NAMESPACE))));
                    if (isEmpty(newSchema)) {
                        newSchema = entity;
                    } else {
                        newSchema.getChildren().addAll(entity.getChildren());
                    }
                }
                addList.add(newSchema);
            }
        }
        allSchemas.removeAll(removeList);
        allSchemas.addAll(addList);
    }

    private static JavaFile createJavaFile(String packagePath, TypeSpec spec) {
        JavaFile javaFile = JavaFile.builder(packagePath, spec).build();
        try {
            javaFile.writeTo(new File(SoapXmlGenerator.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath()
                    .subSequence(0, SoapXmlGenerator.class.getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .getPath()
                            .indexOf(Constants.BUILD))
                    .toString() + "src/main/java"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return javaFile;
    }

    private static JavaFile createJavaFileByField(Field field) {
        TypeSpec.Builder classBuilder = classBuilder(getNameUpperCase(deletePrefix(field.getTypeName())));
        classBuilder.addModifiers(PUBLIC);
        classBuilder.addSuperinterface(Validatable.class);
        classBuilder.addAnnotation(Value.class);
        classBuilder.addAnnotation(Builder.class);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(ON_VALIDATING);
        methodBuilder.addModifiers(PUBLIC);
        methodBuilder.addAnnotation(Override.class);
        methodBuilder.addParameter(Validator.class, VALIDATOR_VARIABLE);
        List<CodeBlock> methodCodeBlockList = new ArrayList<>();
        FieldSpec fieldSpec;
        for (Field innerField : field.getFieldsList()) {
            if (innerField.getType().getName().equals(ComplexType.class.getName())) {
                if (!setJavaFiles.containsKey(innerField.getPrefix() + deletePrefix(innerField.getTypeName()))) {
                    setJavaFiles.put(innerField.getPrefix() + deletePrefix(innerField.getTypeName()), createJavaFileByField(innerField));
                }
                ClassName className = ClassName.get("", getNameUpperCase(deletePrefix(innerField.getTypeName())));
                fieldSpec = innerField.isList()
                        ? FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(List.class), className), getNameLowerCase(innerField.getName()), PRIVATE)
                        .addAnnotation(
                                AnnotationSpec.builder(Singular.class).addMember(VALUE, "$S", "add" + innerField.getName()).build()
                        ).build()
                        : FieldSpec.builder(className, getNameLowerCase(innerField.getName()), PRIVATE).build();

            } else {
                fieldSpec = innerField.isList()
                        ? FieldSpec.builder(ParameterizedTypeName.get(List.class, getTypeForList(innerField.getTypeName())), getNameLowerCase(innerField.getName()), PRIVATE).build()
                        : FieldSpec.builder(innerField.getType(), getNameLowerCase(innerField.getName()), PRIVATE).build();
            }
            if (innerField.isNecessary()) {
                methodCodeBlockList
                        .add(CodeBlock.builder()
                                .add(VALIDATOR, getNameLowerCase(innerField.getName()), getNameLowerCase(innerField.getName()), ValidationExpressions.class, "notNull()")
                                .build()
                        );
            }
            if (isNotEmpty(innerField.getRestrictionList()) && !innerField.getRestrictionList().isEmpty()) {
                Optional<Restriction> minInclusive = innerField.getRestrictionList().stream().filter(rest -> rest.getOperation().equals(GREATER)).findFirst();
                Optional<Restriction> maxInclusive = innerField.getRestrictionList().stream().filter(rest -> rest.getOperation().equals(LESSER)).findFirst();
                if (innerField.getRestrictionList().size() == 2 && (minInclusive.isPresent() && maxInclusive.isPresent())) {
                    methodCodeBlockList
                            .add(CodeBlock.builder()
                                    .add(VALIDATOR_RANGE, getNameLowerCase(innerField.getName()), getNameLowerCase(innerField.getName()), ValidationExpressions.class,
                                            getValidatorMethodName(getNameLowerCase(innerField.getType().getName())), Double.class, "valueOf", minInclusive.get().getValue(),
                                            Double.class, "valueOf", maxInclusive.get().getValue()
                                    )
                                    .build()
                            );
                } else {

                    methodCodeBlockList
                            .add(CodeBlock.builder()
                                    .add(VALIDATOR_CONTAINS, getNameLowerCase(innerField.getName()), getNameLowerCase(innerField.getName()), ValidationExpressions.class,
                                            "containsOther", Arrays.class, "asList",
                                            innerField.getRestrictionList().stream()
                                                    .filter(rest -> rest.getOperation().equals(EQUALS)).map((elem) -> "\"" + elem.getValue() + "\"").collect(Collectors.joining(","))
                                    )
                                    .build()
                            );
                }
            }

            classBuilder.addField(fieldSpec);
        }
        if (!isEmpty(methodCodeBlockList)) {
            methodBuilder.addCode(join(methodCodeBlockList, NEW_LINE));
            classBuilder.addMethod(methodBuilder.build());
        }
        return createJavaFile(packege + ".model." + field.getPrefix(), classBuilder.build());
    }

    private static Field createField(XmlEntity child, XmlEntity parentSchema) {
        Field field = null;
        Map<String, String> attributes = child.getAttributes();
        Class type;
        if (isEmpty(attributes.get(TYPE))) {
            boolean isNecessary = NECESSARY.equals(attributes.get(MIN_OCCURS));
            boolean isList = UNBOUNDED.equals(attributes.get(MAX_OCCURS));
            type = ComplexType.class;
            XmlEntity children = child.getChildren().stream().filter(childElem -> childElem.getTag().equals(COMPLEX_TYPE)).findFirst().get();
            field = Field.builder()
                    .name(attributes.get(NAME))
                    .typeName(attributes.get(NAME))
                    .type(type)
                    .necessary(isNecessary)
                    .list(isList)
                    .prefix(getPrefixByNamespace(parentSchema.getAttributes().get(TARGET_NAMESPACE)))
                    .namespace(parentSchema.getAttributes().get(TARGET_NAMESPACE))
                    .fieldsList(getFieldListByComplexType(children, parentSchema, attributes.get(NAME)))
                    .build();
        } else {
            type = getType(attributes.get(TYPE));
            Optional<XmlEntity> complexType = parentSchema.getChildren().stream()
                    .filter(childSchema -> childSchema.getTag().equals(COMPLEX_TYPE)
                            && childSchema.getAttributes().get(NAME).equals(deletePrefix(attributes.get(TYPE)))).findFirst();
            if (!complexType.isPresent()) throw new MappingGeneratorException("Cannot find complexType by type");
            boolean isNecessary = NECESSARY.equals(attributes.get(MIN_OCCURS));
            boolean isList = UNBOUNDED.equals(attributes.get(MAX_OCCURS));
            field = Field.builder()
                    .name(attributes.get(NAME))
                    .typeName(complexType.get().getAttributes().get(NAME))
                    .type(type)
                    .necessary(isNecessary)
                    .list(isList)
                    .prefix(getPrefixByNamespace(parentSchema.getAttributes().get(TARGET_NAMESPACE)))
                    .namespace(parentSchema.getAttributes().get(TARGET_NAMESPACE))
                    .fieldsList(getFieldListByComplexType(complexType.get(), parentSchema, attributes.get(NAME)))
                    .build();
        }
        return field;
    }

    private static List<Field> getFieldListByComplexType(XmlEntity complexType, XmlEntity schemaComplexType, String parentName) {
        List<Field> fieldList = new LinkedList<>();
        Optional<XmlEntity> seq = complexType.getChildren().stream().filter(
                childComType -> childComType.getTag().equals(SEQUENCE)
        ).findFirst();
        List<XmlEntity> collectChildSeq = seq.isPresent()
                ? seq.get().getChildren().stream().filter(childSeq -> childSeq.getTag().equals(ELEMENT)).collect(Collectors.toList())
                : complexType.getChildren().stream().filter(childComplexType -> childComplexType.getTag().equals(ATTRIBUTE)).collect(Collectors.toList());
        for (XmlEntity element : collectChildSeq) {
            Map<String, String> attributes = element.getAttributes();
            boolean isNecessary = NECESSARY.equals(attributes.get(MIN_OCCURS));
            boolean isList = UNBOUNDED.equals(attributes.get(MAX_OCCURS));
            if (isNotEmpty(attributes.get(REF))) {
                String ref = attributes.get(REF);
                if (isEmpty(ref)) throw new MappingGeneratorException("Cannot find element type");
                Optional<XmlEntity> schemaByRef = allSchemas.stream().filter(schema -> schema.getAttributes().get(TARGET_NAMESPACE).equals(getNamespaceByPrefix(getPrefix(ref)))).findFirst();
                if (!schemaByRef.isPresent())
                    throw new MappingGeneratorException("Cannot find complexType by namespace");
                Optional<XmlEntity> complexTypeByRef = schemaByRef.get().getChildren().stream()
                        .filter(comType -> comType.getAttributes().get(NAME).equals(
                                deletePrefix(schemaByRef.get().getChildren().stream()
                                        .filter(child -> child.getTag().equals(ELEMENT) && child.getAttributes().get(NAME).equals(deletePrefix(ref))).findFirst().get().getAttributes().get(TYPE)))
                        ).findFirst();
                if (!complexTypeByRef.isPresent())
                    throw new MappingGeneratorException("Cannot find complexType by ref");
                Field field = Field.builder()
                        .name(deletePrefix(ref))
                        .typeName(complexTypeByRef.get().getAttributes().get(NAME))
                        .type(ComplexType.class)
                        .necessary(isNecessary)
                        .list(isList)
                        .prefix(getPrefix(ref))
                        .namespace(getNamespaceByPrefix(getPrefix(ref)))
                        .fieldsList(getFieldListByComplexType(complexTypeByRef.get(), schemaByRef.get(), null))
                        .build();
                fieldList.add(field);
            } else if (isNotEmpty(attributes.get(TYPE))) {
                Class type = getType(attributes.get(TYPE));
                if (type.getName().equals(ComplexType.class.getName())) {
                    Optional<XmlEntity> typeByElement = schemaComplexType.getChildren().stream()
                            .filter(child -> deletePrefix(attributes.get(TYPE)).equals(child.getAttributes().get(NAME))).findFirst();
                    if (!typeByElement.isPresent() && !getNamespaceByPrefix(getPrefix(attributes.get(TYPE))).equals(schemaComplexType.getAttributes().get(TARGET_NAMESPACE))) {
                        Optional<XmlEntity> schemaByRef = allSchemas.stream().filter(schema -> schema.getAttributes().get(TARGET_NAMESPACE).equals(getNamespaceByPrefix(
                                getPrefix(element.getAttributes().get(TYPE))))).findFirst();
                        if (!schemaByRef.isPresent())
                            throw new MappingGeneratorException("Cannot find complexType by namespace");
                        typeByElement = schemaByRef.get().getChildren().stream()
                                .filter(comType -> comType.getTag().equals(COMPLEX_TYPE) && comType.getAttributes().get(NAME).equals(deletePrefix(attributes.get(TYPE)))).findFirst();
                        if (!typeByElement.isPresent())
                            throw new MappingGeneratorException("Cannot find complexType by ref");
                        schemaComplexType = schemaByRef.get();
                    } else if (!typeByElement.isPresent())
                        throw new MappingGeneratorException("Cannot find type " + attributes.get(TYPE));
                    if (typeByElement.get().getTag().equals(COMPLEX_TYPE)) {
                        fieldList.add(
                                Field.builder()
                                        .name(attributes.get(NAME))
                                        .typeName(attributes.get(TYPE))
                                        .type(type)
                                        .necessary(isNecessary)
                                        .list(isList)
                                        .prefix(getPrefix(attributes.get(TYPE)))
                                        .namespace(getNamespaceByPrefix(getPrefix(attributes.get(TYPE))))
                                        .fieldsList(getFieldListByComplexType(typeByElement.get(), schemaComplexType, null))
                                        .build()
                        );
                    } else if (typeByElement.get().getTag().equals(SIMPLE_TYPE)) {
                        type = getType(typeByElement.get().find(RESTRICTION).getAttributes().get(BASE));
                        fieldList.add(Field.builder()
                                .name(attributes.get(NAME))
                                .typeName(typeByElement.get().find(RESTRICTION).getAttributes().get(BASE))
                                .type(type)
                                .necessary(isNecessary)
                                .list(isList)
                                .namespace(schemaComplexType.getAttributes().get(TARGET_NAMESPACE))
                                .prefix(getPrefixByNamespace(schemaComplexType.getAttributes().get(TARGET_NAMESPACE)))
                                .build());
                    }
                } else {
                    fieldList.add(Field.builder()
                            .name(attributes.get(NAME))
                            .typeName(attributes.get(TYPE))
                            .type(type)
                            .necessary(isNecessary)
                            .list(isList)
                            .namespace(schemaComplexType.getAttributes().get(TARGET_NAMESPACE))
                            .prefix(getPrefixByNamespace(schemaComplexType.getAttributes().get(TARGET_NAMESPACE)))
                            .build());
                }
            } else if (element.getChildren().stream().filter(child -> child.getTag().equals(COMPLEX_TYPE)).findFirst().isPresent()) {
                XmlEntity innerComplexType = element.getChildren().stream().filter(child -> child.getTag().equals(COMPLEX_TYPE)).findFirst().get();
                Map<String, String> elementAttributes = element.getAttributes();
                isNecessary = NECESSARY.equals(elementAttributes.get(MIN_OCCURS));
                isList = UNBOUNDED.equals(elementAttributes.get(MAX_OCCURS));
                Class type = ComplexType.class;
                fieldList.add(Field.builder()
                        .name(elementAttributes.get(NAME))
                        .typeName(parentName + getNameUpperCase(elementAttributes.get(NAME)))
                        .type(type)
                        .necessary(isNecessary)
                        .list(isList)
                        .namespace(schemaComplexType.getAttributes().get(TARGET_NAMESPACE))
                        .prefix(getPrefixByNamespace(schemaComplexType.getAttributes().get(TARGET_NAMESPACE)))
                        .fieldsList(getFieldListByComplexType(innerComplexType, schemaComplexType, elementAttributes.get(NAME)))
                        .build());
            } else if (element.getChildren().stream().filter(child -> child.getTag().equals(SIMPLE_TYPE)).findFirst().isPresent()) {
                XmlEntity simpleType = element.getChildren().stream().filter(child -> child.getTag().equals(SIMPLE_TYPE)).findFirst().get();
                Class type = getType(simpleType.find(RESTRICTION).getAttributes().get(BASE));
                fieldList.add(Field.builder()
                        .name(attributes.get(NAME))
                        .typeName(element.find(SIMPLE_TYPE).find(RESTRICTION).getAttributes().get(BASE))
                        .type(type)
                        .necessary(isNecessary)
                        .restrictionList(getRestrictionValues(simpleType.find(RESTRICTION).getChildren()))
                        .list(isList)
                        .namespace(schemaComplexType.getAttributes().get(TARGET_NAMESPACE))
                        .prefix(getPrefixByNamespace(schemaComplexType.getAttributes().get(TARGET_NAMESPACE)))
                        .build());
            } else {
                fieldList.add(Field.builder()
                        .name(attributes.get(NAME))
                        .type(Object.class)
                        .necessary(isNecessary)
                        .list(isList)
                        .namespace(schemaComplexType.getAttributes().get(TARGET_NAMESPACE))
                        .prefix(getPrefixByNamespace(schemaComplexType.getAttributes().get(TARGET_NAMESPACE)))
                        .build());
            }
        }
        return fieldList;
    }

    private static CodeBlock getModelFromXmlEntity(Field inputField) {
        CodeBlock codeBlock;
        if (inputField.getType().getName().equals(ComplexType.class.getName())) {
            ClassName className = ClassName.get(packege + ".model." + inputField.getPrefix(), getNameUpperCase(deletePrefix(inputField.getTypeName())));
            List<CodeBlock> codeBlocks = dynamicArrayOf(of(XML_ENTITY_TO_MODEL_MAPPER_LAMBDA, className));
            for (Field field : inputField.getFieldsList()) {
                codeBlocks.add(getModelFromXmlEntity(field));
            }
            codeBlocks.add(of(DOUBLE_TABULATION + BUILD_METHOD));
            ClassName classNameXmlEntityToModelMapper = ClassName.get(XmlEntityToModelMapper.class);
            FieldSpec fieldSpec = FieldSpec.builder(ParameterizedTypeName.get(classNameXmlEntityToModelMapper, className), getNameLowerCase(inputField.getName()) + TO_MODEL, PUBLIC, STATIC, FINAL)
                    .initializer(join(codeBlocks, NEW_LINE))
                    .build();
            String mapperName = getNameUpperCase(deletePrefix(inputField.getTypeName())) + XML_MAPPER;
            if (!interfaceModelFromXmlEntity.containsKey(inputField.getPrefix() + mapperName)) {
                TypeSpec mapper = interfaceBuilder(mapperName).addModifiers(PUBLIC, STATIC)
                        .addField(fieldSpec)
                        .build();
                interfaceModelFromXmlEntity.put(inputField.getPrefix() + mapper.name,
                        createJavaFile(packege + ".mapper." + inputField.getPrefix(), mapper));
            }
            ClassName mapperClassName = ClassName.get(packege + ".mapper." + inputField.getPrefix(), mapperName);
            codeBlock = inputField.isList()
                    ? CodeBlock.builder()
                    .add(
                            DOUBLE_TABULATION + MAP_LIST_TO_MODEL_FOR_COMPLEX_TYPE, getNameLowerCase(inputField.getName()), inputField.getName(), mapperClassName, fieldSpec.name, Collectors.class
                    ).build()
                    : CodeBlock.builder()
                    .add(
                            DOUBLE_TABULATION + MAP_TO_MODEL, getNameLowerCase(inputField.getName()), mapperClassName, fieldSpec.name, inputField.getName()
                    ).build();
        } else {
            codeBlock = inputField.isList()
                    ? CodeBlock.builder()
                    .add(
                            DOUBLE_TABULATION + MAP_LIST_TO_MODEL_FOR_SIMPLE_TYPE, getNameLowerCase(inputField.getName()), inputField.getName(), getTypeFromStringByList(deletePrefix(inputField.getTypeName())),
                            Collectors.class
                    ).build()
                    : CodeBlock.builder()
                    .add(DOUBLE_TABULATION + ADD_FIELD, getNameLowerCase(inputField.getName()), getTypeFromString(deletePrefix(inputField.getTypeName()), inputField.getName()))
                    .build();
        }
        return codeBlock;
    }

    private static CodeBlock getXmlEntityFromModel(Field inputField) {

        CodeBlock codeBlock = CodeBlock.builder()
                .add(DOUBLE_TABULATION + CREATE_TAG + NEW_LINE, inputField.getName())
                .add(DOUBLE_TABULATION + CREATE_PREFIX + NEW_LINE, inputField.getPrefix())
                .add(DOUBLE_TABULATION + CREATE_NAMESPACE + NEW_LINE, inputField.getNamespace())
                .add(DOUBLE_TABULATION + CREATE_NAMESPACE_FIELD, inputField.getPrefix(), inputField.getNamespace())
                .build();

        if (inputField.getType().getName().equals(ComplexType.class.getName())) {
            List<CodeBlock> codeBlocks = dynamicArrayOf(of(XML_ENTITY_FROM_MODEL_MAPPER_LAMBDA, XmlEntity.class));
            codeBlocks.add(codeBlock);
            for (Field field : inputField.getFieldsList()) {
                if (!field.isList() && !field.getType().getName().equals(ComplexType.class.getName()))
                    codeBlocks.add(CodeBlock.builder().add(DOUBLE_TABULATION + CREATE_CHILD).build());
                codeBlocks.add(getXmlEntityFromModel(field));
            }
            codeBlocks.add(of(DOUBLE_TABULATION + CREATE_METHOD));
            ClassName className = ClassName.get(packege + ".model." + inputField.getPrefix(), getNameUpperCase(deletePrefix(inputField.getTypeName())));
            ClassName classNameXmlEntityFromModelMapper = ClassName.get(XmlEntityFromModelMapper.class);
            FieldSpec fieldSpec = FieldSpec.builder(ParameterizedTypeName.get(classNameXmlEntityFromModelMapper, className), getNameLowerCase(inputField.getName()) + FROM_MODEL, PUBLIC, STATIC, FINAL)
                    .initializer(join(codeBlocks, NEW_LINE))
                    .build();
            String mapperName = getNameUpperCase(deletePrefix(inputField.getTypeName())) + XML_MAPPER;
            if (!interfaceXmlEntityFromModel.containsKey(inputField + mapperName)) {
                TypeSpec mapper = interfaceBuilder(getNameUpperCase(deletePrefix(inputField.getTypeName())) + XML_MAPPER).addModifiers(PUBLIC, STATIC)
                        .addField(fieldSpec)
                        .build();
                interfaceXmlEntityFromModel.put(inputField + mapperName, createJavaFile(packege + ".mapper." + inputField.getPrefix(), mapper));
            }
            ClassName mapperClassName = ClassName.get(packege + ".mapper." + inputField.getPrefix(), mapperName);
            codeBlock = inputField.isList() ?
                    CodeBlock.builder()
                            .add(DOUBLE_TABULATION + MAP_LIST_TO_XML_FOR_COMPLEX_TYPE,
                                    getNameUpperCase(inputField.getName()), mapperClassName, fieldSpec.name, Collectors.class)
                            .build()
                    : CodeBlock.builder()
                    .add(DOUBLE_TABULATION + MAP_TO_XML, mapperClassName, fieldSpec.name, getNameUpperCase(inputField.getName()))
                    .build();
        } else {
            codeBlock = inputField.isList()
                    ? CodeBlock.builder()
                    .add(NEW_LINE + DOUBLE_TABULATION + MAP_LIST_TO_XML_FOR_SIMPLE_TYPE,
                            getNameUpperCase(inputField.getName()), XmlEntity.class, "createChild", inputField.getPrefix(), inputField.getNamespace(), inputField.getName(),
                            String.class, Collectors.class)
                    .build()
                    : CodeBlock.builder()
                    .add(codeBlock)
                    .add(NEW_LINE + DOUBLE_TABULATION + CREATE_VALUE, String.class, getMethodName(inputField))
                    .add(NEW_LINE + DOUBLE_TABULATION + BUILD_METHOD)
                    .build();
        }
        return codeBlock;
    }

    private static Operation getOperationByMessage(List<XmlEntity> messages, List<XmlEntity> operationChildren, String operationName, boolean isServer) {
        Operation.OperationBuilder operationBuilder = Operation.builder();
        operationBuilder.name(operationName);
        for (XmlEntity child : operationChildren) {
            Optional<XmlEntity> optionalMessage = messages.stream().filter(message -> message.getAttributes().get(NAME).equals(deletePrefix(child.getAttributes().get(MESSAGE)))).findFirst();
            if (!optionalMessage.isPresent())
                throw new MappingGeneratorException("Cannot find message for operation " + deletePrefix(child.getAttributes().get(MESSAGE)));
            String fieldName = deletePrefix(optionalMessage.get().find(PART).getAttributes().get(ELEMENT));
            Optional<Field> operaion = listTypes.stream().filter(field -> field.getName().equals(fieldName)).findFirst();
            if (!operaion.isPresent()) throw new MappingGeneratorException("Cannot find operation" + fieldName);
            Field field = operaion.get();
            Field operationfield = Field.builder()
                    .name(optionalMessage.get().getAttributes().get(NAME))
                    .type(field.getType())
                    .typeName(field.getTypeName())
                    .prefix(field.getPrefix())
                    .namespace(field.getNamespace())
                    .list(field.isList())
                    .necessary(field.isNecessary())
                    .fieldsList(field.getFieldsList())
                    .restrictionList(field.getRestrictionList())
                    .parentName(field.getParentName())
                    .build();
            if (isServer) {
                switch (child.getTag()) {
                    case "input":
                        operationBuilder.input(operationfield);
                        break;
                    case "output":
                        operationBuilder.output(operationfield);
                        break;
                    case "fault":
                        operationBuilder.addFault(operationfield);
                        break;
                }
            } else {
                switch (child.getTag()) {
                    case "input":
                        operationBuilder.output(operationfield);
                        break;
                    case "output":
                        operationBuilder.input(operationfield);
                        break;
                    case "fault":
                        operationBuilder.addFault(operationfield);
                        break;
                }
            }

        }
        return operationBuilder.build();
    }

    private static Class getType(String type) {
        switch (deletePrefix(type)) {
            case "string":
                return String.class;
            case "byte":
                return byte.class;
            case "boolean":
                return boolean.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "decimal":
                return double.class;
            case "long":
                return long.class;
            case "int":
                return int.class;
            case "integer":
                return int.class;
            case "dateTime":
                return Date.class;
            case "time":
                return Date.class;
            case "date":
                return Date.class;
            default:
                return ComplexType.class;
        }
    }

    private static CodeBlock getTypeFromString(String value, String nameParameter) {
        CodeBlock.Builder builder = CodeBlock.builder();
        switch (value) {
            case "string":
                builder.add(GET_VALUE_BY_TAG, nameParameter);
                return builder.build();
            case "boolean":
                builder.add("$T.parseBoolean(" + GET_VALUE_BY_TAG + ")", Boolean.class, nameParameter);
                return builder.build();
            case "byte":
                builder.add("$T.parseByte(" + GET_VALUE_BY_TAG + ")", Byte.class, nameParameter);
                return builder.build();
            case "float":
                builder.add("$T.parseFloat(" + GET_VALUE_BY_TAG + ")", Float.class, nameParameter);
                return builder.build();
            case "double":
                builder.add("$T.parseDouble(" + GET_VALUE_BY_TAG + ")", Double.class, nameParameter);
                return builder.build();
            case "decimal":
                builder.add("$T.parseDouble(" + GET_VALUE_BY_TAG + ")", Double.class, nameParameter);
                return builder.build();
            case "long":
                builder.add("$T.parseLong(" + GET_VALUE_BY_TAG + ")", Long.class, nameParameter);
                return builder.build();
            case "int":
                builder.add("$T.parseInt(" + GET_VALUE_BY_TAG + ")", Integer.class, nameParameter);
                return builder.build();
            case "integer":
                builder.add("$T.parseInt(" + GET_VALUE_BY_TAG + ")", Integer.class, nameParameter);
                return builder.build();
            case "dateTime":
                builder.add("$T.parse($T.$N, " + GET_VALUE_BY_TAG + ")", DateExtensions.class, DateConstants.class, YYYY_MM_DD_DASH, nameParameter);
                return builder.build();
            case "time":
                builder.add("$T.parse($T.$N, " + GET_VALUE_BY_TAG + ")", DateExtensions.class, DateConstants.class, HH_MM_SS_24H, nameParameter);
                return builder.build();
            case "date":
                builder.add("$T.parse($T.$N, " + GET_VALUE_BY_TAG + ")", DateExtensions.class, DateConstants.class, YYYY_MM_DD_T_HH_MM_SS_SSSXXX, nameParameter);
                return builder.build();
            default:
                builder.add(GET_VALUE_BY_TAG, nameParameter);
                return builder.build();
        }
    }

    private static CodeBlock getTypeFromStringByList(String value) {
        CodeBlock.Builder builder = CodeBlock.builder();
        switch (value) {
            case "string":
                builder.add(CHILD_GET_VALUE);
                return builder.build();
            case "boolean":
                builder.add("$T.parseBoolean(" + CHILD_GET_VALUE + ")", Boolean.class);
                return builder.build();
            case "byte":
                builder.add("$T.parseByte(" + CHILD_GET_VALUE + ")", Boolean.class);
                return builder.build();
            case "float":
                builder.add("$T.parseFloat(" + CHILD_GET_VALUE + ")", Float.class);
                return builder.build();
            case "double":
                builder.add("$T.parseDouble(" + CHILD_GET_VALUE + ")", Double.class);
                return builder.build();
            case "decimal":
                builder.add("$T.parseDouble(" + CHILD_GET_VALUE + ")", Double.class);
                return builder.build();
            case "long":
                builder.add("$T.parseLong(" + CHILD_GET_VALUE + ")", Long.class);
                return builder.build();
            case "int":
                builder.add("$T.parseInt(" + CHILD_GET_VALUE + ")", Integer.class);
                return builder.build();
            case "integer":
                builder.add("$T.parseInt(" + CHILD_GET_VALUE + ")", Integer.class);
                return builder.build();
            case "dateTime":
                builder.add("($T.parse($T.$N," + CHILD_GET_VALUE + "))", DateTimeExtensions.class, DateConstants.class, "YYYY_MM_DD_DASH_FORMAT");
                return builder.build();
            case "time":
                builder.add("($T.parse($T.$N," + CHILD_GET_VALUE + "))", DateTimeExtensions.class, DateConstants.class, HH_MM_SS_24H_FORMAT);
                return builder.build();
            case "date":
                builder.add("($T.parse($T.$N," + CHILD_GET_VALUE + "))", DateTimeExtensions.class, DateConstants.class, YYYY_MM_DD_T_HH_MM_SS_SSSXXX_FORMAT);
                return builder.build();
            default:
                builder.add(CHILD_GET_VALUE);
                return builder.build();
        }
    }

    private static Class getTypeForList(String type) {
        switch (deletePrefix(type)) {
            case "string":
                return String.class;
            case "byte":
                return Byte.class;
            case "boolean":
                return Boolean.class;
            case "float":
                return Float.class;
            case "double":
                return Double.class;
            case "decimal":
                return Double.class;
            case "long":
                return Long.class;
            case "int":
                return Integer.class;
            case "integer":
                return Integer.class;
            case "dateTime":
                return Date.class;
            case "time":
                return Date.class;
            case "date":
                return Date.class;
            default:
                return Object.class;
        }
    }

    private static List<Restriction> getRestrictionValues(List<XmlEntity> childRest) {
        List<Restriction> restrictionList = new ArrayList<>();
        Optional<XmlEntity> minInclusive = childRest.stream().filter(child -> child.getTag().equals(MIN_INCLUSIVE)).findFirst();
        Optional<XmlEntity> maxInclusive = childRest.stream().filter(child -> child.getTag().equals(MAX_INCLUSIVE)).findFirst();
        if (minInclusive.isPresent() && maxInclusive.isPresent()) {
            restrictionList.add(
                    Restriction.builder()
                            .operation(GREATER)
                            .value(minInclusive.get().getAttributes().get(VALUE))
                            .build()
            );
            restrictionList.add(
                    Restriction.builder()
                            .operation(LESSER)
                            .value(maxInclusive.get().getAttributes().get(VALUE))
                            .build()
            );
            return restrictionList;
        }
        for (XmlEntity enumeration : childRest.stream().filter(child -> child.getTag().equals(ENUMERATION)).collect(Collectors.toList())) {
            restrictionList.add(
                    Restriction.builder()
                            .operation(EQUALS)
                            .value(enumeration.getAttributes().get(VALUE))
                            .build()
            );
        }
        return restrictionList;
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

    private static String getMethodName(Field inputField) {
        return inputField.getType().equals(boolean.class) ?
                "is" + getNameUpperCase(inputField.getName())
                : "get" + getNameUpperCase(inputField.getName());
    }

    private static String deletePrefix(String value) {
        return value.contains(":") ? value.substring(value.indexOf(":") + 1) : value;
    }

    private static String getPrefix(String value) {
        return value.contains(":") ? value.substring(0, value.indexOf(":")) : value;
    }

    private static String getNameUpperCase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private static String getNameLowerCase(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    private static String getPrefixByNamespace(String namespace) {
        for (Entry<String, String> entries : namespaces.entrySet()) {
            if (entries.getValue().equals(namespace)) {
                return entries.getKey();
            }
        }
        throw new MappingGeneratorException("Not found prefix for namespace " + namespace);
    }

    private static String getNamespaceByPrefix(String prefix) {
        String namespace = namespaces.get(prefix);
        if (isEmpty(namespace)) throw new MappingGeneratorException("Not found prefix for namespace " + namespace);

        return namespace;
    }
}

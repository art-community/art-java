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

import com.squareup.javapoet.*;
import lombok.Builder;
import lombok.*;
import lombok.experimental.*;
import ru.art.generator.soap.model.*;
import ru.art.service.validation.*;
import static com.squareup.javapoet.CodeBlock.*;
import static com.squareup.javapoet.TypeSpec.*;
import static javax.lang.model.element.Modifier.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.StringExtensions.*;
import static ru.art.generator.mapper.constants.Constants.PathAndPackageConstants.*;
import static ru.art.generator.soap.constants.Constants.*;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.*;
import static ru.art.generator.soap.factory.TypeFactory.*;
import static ru.art.generator.soap.service.SoapGeneratorService.*;
import java.io.*;
import java.util.*;

@UtilityClass
public class JavaFileFactory {

    private static HashMap<String, JavaFile> setJavaFiles = new HashMap<>();

    public static JavaFile createJavaFile(String packagePath, TypeSpec spec) {
        JavaFile javaFile = JavaFile.builder(packagePath, spec).build();
        try {
            javaFile.writeTo(new File(SRC_MAIN_JAVA_ABSOLUTE_PATH.get()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return javaFile;
    }


    public static JavaFile createJavaFileByField(Field field, String packageString) {
        TypeSpec.Builder classBuilder = classBuilder(firstLetterToUpperCase(field.getTypeName()));
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
            if (isObject(innerField.getType())) {
                checkAndAddJavaClass(innerField, packageString);
                ClassName className = ClassName.get(packageString + ".model." + innerField.getPrefix(), firstLetterToUpperCase(innerField.getTypeName()));
                fieldSpec = innerField.isList()
                        ? FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(List.class), className),
                        firstLetterToLowerCase(innerField.getName()), PRIVATE).addAnnotation(addSingularAnnotation(innerField)).build()
                        : FieldSpec.builder(className, firstLetterToLowerCase(innerField.getName()), PRIVATE).build();
            } else {
                fieldSpec = innerField.isList()
                        ? FieldSpec.builder(ParameterizedTypeName.get(List.class,
                        TypeFactory.getTypeByString(innerField.getTypeName())),
                        firstLetterToLowerCase(innerField.getName()),
                        PRIVATE).build()
                        : FieldSpec.builder(innerField.getType(), firstLetterToLowerCase(innerField.getName()), PRIVATE).build();
            }
            if (innerField.isNecessary()) {
                methodCodeBlockList
                        .add(CodeBlock.builder()
                                .add(VALIDATOR, firstLetterToLowerCase(innerField.getName()), firstLetterToLowerCase(innerField.getName()),
                                        ValidationExpressions.class, "notNull()")
                                .build()
                        );
            }
            //ToDo: This code for work with restriction
//      if (isNotEmpty(innerField.getRestrictionList()) && !innerField.getRestrictionList().isEmpty()) {
//        Optional<Restriction> minInclusive = innerField.getRestrictionList().stream()
//            .filter(rest -> rest.getOperation().equals(RestrictionOperation.MAX_EXCLUSIVE)).findFirst();
//        Optional<Restriction> maxInclusive = innerField.getRestrictionList().stream()
//            .filter(rest -> rest.getOperation().equals(RestrictionOperation.MIN_EXCLUSIVE)).findFirst();
//        if (innerField.getRestrictionList().size() == 2 && (minInclusive.isPresent() && maxInclusive.isPresent())) {
//          methodCodeBlockList
//              .add(CodeBlock.builder()
//                  .add(VALIDATOR_RANGE, getNameLowerCase(innerField.getName()), getNameLowerCase(innerField.getName()),
//                      ValidationExpressions.class,
//                      getValidatorMethodName(getNameLowerCase(innerField.getType().getName())), Double.class, "valueOf",
//                      minInclusive.get().getValue(),
//                      Double.class, "valueOf", maxInclusive.get().getValue()
//                  )
//                  .build()
//              );
//        } else {
//          methodCodeBlockList
//              .add(CodeBlock.builder()
//                  .add(VALIDATOR_CONTAINS, getNameLowerCase(innerField.getName()),
//                      getNameLowerCase(innerField.getName()), ValidationExpressions.class,
//                      "containsOther", Arrays.class, "asList",
//                      innerField.getRestrictionList().stream()
//                          .filter(rest -> rest.getOperation().equals(RestrictionOperation.ENUMERATION))
//                          .map((elem) -> "\"" + elem.getValue() + "\"").collect(Collectors.joining(","))
//                  )
//                  .build()
//              );
//        }
//      }
            classBuilder.addField(fieldSpec);
        }
        if (!isEmpty(methodCodeBlockList)) {
            methodBuilder.addCode(join(methodCodeBlockList, NEW_LINE));
            classBuilder.addMethod(methodBuilder.build());
        }
        return createJavaFile(packageString + DOT_MODEL_DOT + field.getPrefix(), classBuilder.build());
    }

    private static boolean checkDuplicateCreatedJavaClass(Field field) {
        return setJavaFiles.containsKey(field.getPrefix() + field.getTypeName());
    }

    private static void checkAndAddJavaClass(Field field, String packageString) {
        if (checkDuplicateCreatedJavaClass(field)) {
            return;
        }
        setJavaFiles.put(field.getPrefix() + field.getTypeName(), createJavaFileByField(field, packageString));
    }

    private static AnnotationSpec addSingularAnnotation(Field field) {
        return AnnotationSpec.builder(Singular.class).addMember(VALUE, "$S", "add" + field.getName()).build();
    }
}

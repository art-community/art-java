package ru.art.generator.soap.factory;

import static com.squareup.javapoet.CodeBlock.join;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.springframework.core.annotation.AnnotationUtils.VALUE;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.StringConstants.NEW_LINE;
import static ru.art.core.extension.StringExtensions.firstLetterToLowerCase;
import static ru.art.core.extension.StringExtensions.firstLetterToUpperCase;
import static ru.art.generator.common.constants.Constants.PathAndPackageConstants.SRC_MAIN_JAVA;
import static ru.art.generator.mapper.constants.Constants.PathAndPackageConstants.DOT_MODEL_DOT;
import static ru.art.generator.soap.constants.Constants.ON_VALIDATING;
import static ru.art.generator.soap.constants.Constants.ToXmlModelConstants.VALIDATOR;
import static ru.art.generator.soap.constants.Constants.VALIDATOR_VARIABLE;
import static ru.art.generator.soap.factory.TypeFactory.isObject;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.UtilityClass;
import ru.art.generator.soap.constants.Constants;
import ru.art.generator.soap.model.Field;
import ru.art.generator.soap.service.SourceCodeGenService;
import ru.art.service.validation.Validatable;
import ru.art.service.validation.ValidationExpressions;
import ru.art.service.validation.Validator;

@UtilityClass
public class JavaFileFactory {

  private static HashMap<String, JavaFile> setJavaFiles = new HashMap<>();

  public static JavaFile createJavaFile(String packagePath, TypeSpec spec) {
    JavaFile javaFile = JavaFile.builder(packagePath, spec).build();
    try {
      javaFile.writeTo(new File(SourceCodeGenService.class.getProtectionDomain()
          .getCodeSource()
          .getLocation()
          .getPath()
          .subSequence(0, SourceCodeGenService.class.getProtectionDomain()
              .getCodeSource()
              .getLocation()
              .getPath()
              .indexOf(Constants.BUILD))
          .toString() + SRC_MAIN_JAVA));
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

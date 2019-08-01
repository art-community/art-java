package ru.adk.generator.spec.common.operations;

import com.squareup.javapoet.FieldSpec;
import ru.adk.generator.mapper.constants.Constants;
import ru.adk.generator.spec.common.constants.SpecificationType;
import ru.adk.generator.spec.common.exception.SpecificationTypeDefinitionException;
import ru.adk.generator.spec.http.proxyspec.operations.HttpProxySpecificationClassGenerator;
import ru.adk.generator.spec.http.servicespec.operations.HttpServiceSpecificationClassGenerator;
import static java.text.MessageFormat.format;
import static javax.lang.model.element.Modifier.*;
import static ru.adk.generator.common.constants.Constants.SymbolsAndFormatting.STRING_PATTERN;
import static ru.adk.generator.spec.common.constants.CommonSpecGeneratorConstants.SERVICE_ID;
import static ru.adk.generator.spec.common.constants.SpecExceptionConstants.DefinitionExceptions.UNABLE_TO_DEFINE_SPECIFICATION_TYPE;
import static ru.adk.generator.spec.common.operations.IdCalculator.getSpecificationId;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Interface containing common operations for all specification's generators.
 */
public interface ConstantsFieldSpecGenerator {

    /**
     * Method generates service id from service's name.
     *
     * @param serviceClass - class of service.
     * @return FieldSpec containing service id.
     */
    static FieldSpec generateServiceId(Class<?> serviceClass, SpecificationType specType) {
        return FieldSpec.builder(String.class, SERVICE_ID, PUBLIC, FINAL)
                .initializer(STRING_PATTERN, getSpecificationId(serviceClass.getSimpleName(), specType))
                .build();
    }

    /**
     * Method generating block of proxy specification's constants,
     * implemented from default specification.
     *
     * @return methods' ids as string constants.
     */
    static Iterable<FieldSpec> generateProxySpecFieldsBlock() {
        ArrayList<FieldSpec> fieldSpecs = new ArrayList<>();
        fieldSpecs.add(FieldSpec.builder(String.class, "host").addModifiers(PUBLIC, FINAL).build());
        fieldSpecs.add(FieldSpec.builder(Integer.class, "port").addModifiers(PUBLIC, FINAL).build());
        fieldSpecs.add(FieldSpec.builder(String.class, "path").addModifiers(PUBLIC, FINAL).build());
        return fieldSpecs;
    }

    /**
     * Method generating block of methods' ids constants.
     *
     * @return methods' ids as string constants.
     * @throws SpecificationTypeDefinitionException when it's unable to define specification's type
     */
    static Iterable<FieldSpec> generateMethodIdsConstantsBlock(SpecificationType specType)
            throws SpecificationTypeDefinitionException {
        switch (specType) {
            case httpServiceSpec:
                return Stream.of(HttpServiceSpecificationClassGenerator.methodIds.toArray())
                        .map(id -> FieldSpec.builder(String.class, id.toString())
                                .addModifiers(PUBLIC, STATIC, FINAL)
                                .initializer(Constants.SymbolsAndFormatting.STRING_PATTERN, id.toString())
                                .build())
                        .collect(Collectors.toList());
            case httpProxySpec:
                return Stream.of(HttpProxySpecificationClassGenerator.methodIds.toArray())
                        .map(id -> FieldSpec.builder(String.class, id.toString())
                                .addModifiers(PUBLIC, STATIC, FINAL)
                                .initializer(Constants.SymbolsAndFormatting.STRING_PATTERN, id.toString())
                                .build())
                        .collect(Collectors.toList());
            case soapServiceSpec:
                //TODO replace empty array to methodId array
                return Stream.of(new ArrayList<>().toArray())
                        .map(id -> FieldSpec.builder(String.class, id.toString())
                                .addModifiers(PUBLIC, STATIC, FINAL)
                                .initializer(Constants.SymbolsAndFormatting.STRING_PATTERN, id.toString())
                                .build())
                        .collect(Collectors.toList());
            case soapProxySpec:
                //TODO replace empty array to methodId array
                return Stream.of(new ArrayList<>().toArray())
                        .map(id -> FieldSpec.builder(String.class, id.toString())
                                .addModifiers(PUBLIC, STATIC, FINAL)
                                .initializer(Constants.SymbolsAndFormatting.STRING_PATTERN, id.toString())
                                .build())
                        .collect(Collectors.toList());
            case grpcServiceSpec:
                //TODO replace empty array to methodId array
                return Stream.of(new ArrayList<>().toArray())
                        .map(id -> FieldSpec.builder(String.class, id.toString())
                                .addModifiers(PUBLIC, STATIC, FINAL)
                                .initializer(Constants.SymbolsAndFormatting.STRING_PATTERN, id.toString())
                                .build())
                        .collect(Collectors.toList());
            case grpcProxySpec:
                //TODO replace empty array to methodId array
                return Stream.of(new ArrayList<>().toArray())
                        .map(id -> FieldSpec.builder(String.class, id.toString())
                                .addModifiers(PUBLIC, STATIC, FINAL)
                                .initializer(Constants.SymbolsAndFormatting.STRING_PATTERN, id.toString())
                                .build())
                        .collect(Collectors.toList());
        }
        throw new SpecificationTypeDefinitionException(format(UNABLE_TO_DEFINE_SPECIFICATION_TYPE, specType.name()));
    }
}
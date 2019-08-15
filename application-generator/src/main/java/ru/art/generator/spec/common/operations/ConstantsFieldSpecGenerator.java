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

package ru.art.generator.spec.common.operations;

import com.squareup.javapoet.FieldSpec;
import ru.art.generator.mapper.constants.Constants;
import ru.art.generator.spec.common.constants.SpecificationType;
import ru.art.generator.spec.common.exception.SpecificationTypeDefinitionException;
import ru.art.generator.spec.http.proxyspec.operations.HttpProxySpecificationClassGenerator;
import ru.art.generator.spec.http.servicespec.operations.HttpServiceSpecificationClassGenerator;
import static java.text.MessageFormat.format;
import static javax.lang.model.element.Modifier.*;
import static ru.art.generator.common.constants.Constants.SymbolsAndFormatting.STRING_PATTERN;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.SERVICE_ID;
import static ru.art.generator.spec.common.constants.SpecExceptionConstants.DefinitionExceptions.UNABLE_TO_DEFINE_SPECIFICATION_TYPE;
import static ru.art.generator.spec.common.operations.IdCalculator.getSpecificationId;
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
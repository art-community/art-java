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

package ru.art.generator.spec.http.servicespec.operations;

import com.squareup.javapoet.*;
import com.squareup.javapoet.MethodSpec.*;
import ru.art.core.caster.*;
import ru.art.entity.*;
import ru.art.generator.spec.common.annotation.*;
import ru.art.generator.spec.common.constants.*;
import ru.art.generator.spec.common.exception.*;
import ru.art.generator.spec.http.common.annotation.*;
import ru.art.generator.spec.http.servicespec.annotation.*;
import ru.art.generator.spec.http.servicespec.constants.*;
import ru.art.generator.spec.http.servicespec.model.*;
import ru.art.http.client.interceptor.*;
import ru.art.http.constants.*;
import ru.art.service.constants.*;
import static java.text.MessageFormat.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.generator.common.constants.Constants.*;
import static ru.art.generator.common.operations.CommonOperations.*;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.*;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.ExecuteMethodConstants.*;
import static ru.art.generator.spec.common.constants.SpecExceptionConstants.DefinitionExceptions.*;
import static ru.art.generator.spec.common.constants.SpecificationType.*;
import static ru.art.generator.spec.common.operations.AnnotationsChecker.*;
import static ru.art.generator.spec.http.common.constants.HttpSpecConstants.Errors.*;
import static ru.art.generator.spec.http.common.constants.HttpSpecConstants.Methods.*;
import static ru.art.generator.spec.http.servicespec.constants.HttpServiceSpecAnnotations.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Interface contains operations, which helps in specification's parts generation.
 */
public interface HttpServiceAuxiliaryOperations {

    /**
     * Method adds statement to method builder, which describes
     * execution of certain service's method.
     *
     * @param currentMethod - method to be executed
     * @param methodBuilder - builder for MethodSpec
     * @param annotations   - model for annotations to check.
     * @param methodId      - id of current method.
     * @return methodBuilder with created line
     * @throws MethodConsumesWithoutParamsException is thrown when method has @HttpConsumes annotation, but doesn't have input parameter.
     */
    static Builder addMethodStatementForExecute(Method currentMethod, Builder methodBuilder, HttpServiceMethodsAnnotations annotations, String methodId)
            throws MethodConsumesWithoutParamsException {
        if (annotations.isHasRequestMapper() && annotations.isHasResponseMapper())
            if (currentMethod.getParameters().length != 0)
                methodBuilder.addStatement(EXEC_RESP_AND_REQ_SERVICE_SPEC, methodId,
                        currentMethod.getName(),
                        currentMethod.getParameters()[0].getType());
            else
                throw new MethodConsumesWithoutParamsException(format(METHOD_NAME_STRING, currentMethod.getName()) + METHOD_CONSUMES_WITHOUT_PARAMS);
        else if (annotations.isHasRequestMapper())
            if (currentMethod.getParameters().length != 0)
                methodBuilder.addStatement(EXEC_REQ_SERVICE_SPEC, methodId,
                        currentMethod.getName(),
                        currentMethod.getParameters()[0].getType());
            else
                throw new MethodConsumesWithoutParamsException(format(METHOD_NAME_STRING, currentMethod.getName()) + METHOD_CONSUMES_WITHOUT_PARAMS);
        else
            methodBuilder.addStatement(annotations.isHasResponseMapper() ? EXEC_RESP_SERVICE_SPEC : EXEC_NO_RESP_OR_REQ_SERVICE_SPEC,
                    methodId,
                    currentMethod.getName());
        return methodBuilder;
    }

    /**
     * Method defines which static imports are needed to be added
     * and adds it to javaFileBuilder.
     *
     * @param javaFileBuilder   - builder for java file.
     * @param service           - service model interface.
     * @param importsForMethods - map with boolean values.
     *                          to define imports witch need to be included based on each method of service.
     */
    static void addStaticImports(JavaFile.Builder javaFileBuilder, Class<?> service, Map<Method, StaticImports> importsForMethods) {
        StaticImports importsNeeded = new StaticImports();
        for (Map.Entry<Method, StaticImports> entry : importsForMethods.entrySet()) {
            if (isNotEmpty(entry.getValue())) {
                if (!importsNeeded.isHasCaster() && entry.getValue().isHasCaster()) {
                    javaFileBuilder.addStaticImport(Caster.class, CAST);
                    importsNeeded.setHasCaster(true);
                }

                if (entry.getValue().isHasServiceMethods()) {
                    if (!importsNeeded.isHasServiceMethods()) {
                        javaFileBuilder.addStaticImport(service, WILDCARD);
                        javaFileBuilder.addStaticImport(MimeToContentTypeMapper.class, WILDCARD);
                        importsNeeded.setHasServiceMethods(true);
                    }

                    try {
                        if (methodHasAnnotation(entry.getKey(), RequestMapper.class, httpServiceSpec)) {
                            Class reqMapper = Object.class.equals(entry.getKey().getAnnotation(RequestMapper.class).forAll())
                                    ? entry.getKey().getAnnotation(RequestMapper.class).httpService()
                                    : entry.getKey().getAnnotation(RequestMapper.class).forAll();
                            if (!isClassPrimitive(reqMapper)) {
                                if (reqMapper.getSimpleName().contains(REQUEST + RESPONSE)) {
                                    for (Class innerClass : reqMapper.getDeclaredClasses()) {
                                        if (innerClass.getSimpleName().contains(REQUEST + MAPPER)) {
                                            reqMapper = innerClass;
                                            break;
                                        }
                                    }
                                }
                                javaFileBuilder.addStaticImport(reqMapper, WILDCARD);
                            }
                        }

                        if (methodHasAnnotation(entry.getKey(), ResponseMapper.class, httpServiceSpec)) {
                            Class respMapper = Object.class.equals(entry.getKey().getAnnotation(ResponseMapper.class).forAll())
                                    ? entry.getKey().getAnnotation(ResponseMapper.class).httpService()
                                    : entry.getKey().getAnnotation(ResponseMapper.class).forAll();
                            if (!isClassPrimitive(respMapper)) {
                                if (respMapper.getSimpleName().contains(REQUEST + RESPONSE)) {
                                    for (Class innerClass : respMapper.getDeclaredClasses()) {
                                        if (innerClass.getSimpleName().contains(RESPONSE + MAPPER)) {
                                            respMapper = innerClass;
                                            break;
                                        }
                                    }
                                }
                                javaFileBuilder.addStaticImport(respMapper, WILDCARD);
                            }
                        }
                    } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
                        printError(UNABLE_TO_DEFINE_NECESSITY_OF_IMPORT + exception.getMessage());
                    }
                }

                if (!importsNeeded.isHasValidationPolicy() && entry.getValue().isHasValidationPolicy()) {
                    javaFileBuilder.addStaticImport(RequestValidationPolicy.class, WILDCARD);
                    importsNeeded.setHasValidationPolicy(true);
                }

                if (!importsNeeded.isHasPrimitiveMapper() && entry.getValue().isHasPrimitiveMapper()) {
                    javaFileBuilder.addStaticImport(PrimitiveMapping.class, WILDCARD);
                    importsNeeded.setHasPrimitiveMapper(true);
                }

                if (!importsNeeded.isHasInterceptor() && entry.getValue().isHasInterceptor()) {
                    javaFileBuilder.addStaticImport(HttpClientInterceptor.class, WILDCARD);
                    importsNeeded.setHasInterceptor(true);
                }
            }
        }
    }

    /**
     * Method checks if method has annotations.
     * If it's impossible to define specification type or annotation an error is printed.
     *
     * @param currentMethod - method to check.
     * @param specType      - generating type of specification.
     * @param annotations   - model for annotations to check.
     */
    static void checkHttpServiceAnnotations(Method currentMethod, SpecificationType specType, HttpServiceMethodsAnnotations annotations) {
        try {
            annotations.setHasGet(methodHasAnnotation(currentMethod, HttpGet.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasPost(methodHasAnnotation(currentMethod, HttpPost.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasPut(methodHasAnnotation(currentMethod, HttpPut.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasConnect(methodHasAnnotation(currentMethod, HttpConnect.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasDelete(methodHasAnnotation(currentMethod, HttpDelete.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasTrace(methodHasAnnotation(currentMethod, HttpTrace.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasHead(methodHasAnnotation(currentMethod, HttpHead.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasOptions(methodHasAnnotation(currentMethod, HttpOptions.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasPatch(methodHasAnnotation(currentMethod, HttpPatch.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasConsumes(methodHasAnnotation(currentMethod, Consumes.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasProduces(methodHasAnnotation(currentMethod, Produces.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasFromBody(methodHasAnnotation(currentMethod, FromBody.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasFromMultipart(methodHasAnnotation(currentMethod, FromMultipart.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasFromPathParams(methodHasAnnotation(currentMethod, FromPathParams.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasFromQueryParams(methodHasAnnotation(currentMethod, FromQueryParams.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasListen(methodHasAnnotation(currentMethod, Listen.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasNotNull(methodHasAnnotation(currentMethod, NotNull.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasValidatable(methodHasAnnotation(currentMethod, Validatable.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasRequestMapper(methodHasAnnotation(currentMethod, RequestMapper.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasResponseMapper(methodHasAnnotation(currentMethod, ResponseMapper.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasReqInterceptor(methodHasAnnotation(currentMethod, RequestInterceptor.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasRespInterceptor(methodHasAnnotation(currentMethod, ResponseInterceptor.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
    }

    /**
     * Method defines pattern used for generating method
     * by one of http method annotation.
     * Method can only be used for http method annotations:
     *
     * @param httpMethodAnnotationType - type on annotation to check.
     * @return pattern for method.
     * @HttpPost, @HttpGet, @HttpDelete, @HttpHead, @HttpOptions, @HttpPatch, @HttpPut, @HttpTrace, @HttpConnect.
     */
    static String getBlockPatternForHttpMethod(HttpServiceSpecAnnotations httpMethodAnnotationType) {
        return httpMethodAnnotationType.getClassName().equals(HTTP_POST.getClassName()) ? POST
                : httpMethodAnnotationType.getClassName().equals(HTTP_GET.getClassName()) ? GET
                : httpMethodAnnotationType.getClassName().equals(HTTP_DELETE.getClassName()) ? DELETE
                : httpMethodAnnotationType.getClassName().equals(HTTP_HEAD.getClassName()) ? HEAD
                : httpMethodAnnotationType.getClassName().equals(HTTP_OPTIONS.getClassName()) ? OPTIONS
                : httpMethodAnnotationType.getClassName().equals(HTTP_PATCH.getClassName()) ? PATCH
                : httpMethodAnnotationType.getClassName().equals(HTTP_PUT.getClassName()) ? PUT
                : httpMethodAnnotationType.getClassName().equals(HTTP_TRACE.getClassName()) ? TRACE
                : CONNECT;
    }

    /**
     * Method gets codeBlock from generatedFields map by http method's annotation.
     * GeneratedFields map can't contain several http method's annotation.
     *
     * @param generatedFields - map of fields witch were generated in CodeBlock form.
     * @return Generated CodeBlock.
     */
    static CodeBlock generatedFieldsHttpMethodValue(Map<HttpServiceSpecAnnotations, CodeBlock> generatedFields) {
        return generatedFields.containsKey(HTTP_POST) ? generatedFields.get(HTTP_POST)
                : generatedFields.containsKey(HTTP_GET) ? generatedFields.get(HTTP_GET)
                : generatedFields.containsKey(HTTP_CONNECT) ? generatedFields.get(HTTP_CONNECT)
                : generatedFields.containsKey(HTTP_DELETE) ? generatedFields.get(HTTP_DELETE)
                : generatedFields.containsKey(HTTP_HEAD) ? generatedFields.get(HTTP_HEAD)
                : generatedFields.containsKey(HTTP_OPTIONS) ? generatedFields.get(HTTP_OPTIONS)
                : generatedFields.containsKey(HTTP_PATCH) ? generatedFields.get(HTTP_PATCH)
                : generatedFields.containsKey(HTTP_PUT) ? generatedFields.get(HTTP_PUT)
                : generatedFields.get(HTTP_TRACE);
    }

    /**
     * Method checks if one of annotations, which aren't allow
     * request, was generates successfully.
     * Annotations, which aren't allow request: @HttpTrace, @HttpHead, @HttpConnect.
     *
     * @param generatedFields - map of fields witch were generated in CodeBlock form.
     * @return - true: if one of annotations was generated.
     * - false: if none of annotations was generated.
     */
    static boolean annotationWithNoRequestWasGenerated(Map<HttpServiceSpecAnnotations, CodeBlock> generatedFields) {
        return generatedFields.containsKey(HTTP_TRACE)
                || generatedFields.containsKey(HTTP_HEAD)
                || generatedFields.containsKey(HTTP_CONNECT);
    }
}

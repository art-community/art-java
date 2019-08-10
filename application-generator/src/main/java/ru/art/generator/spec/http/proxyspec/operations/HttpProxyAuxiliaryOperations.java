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

package ru.art.generator.spec.http.proxyspec.operations;

import com.squareup.javapoet.*;
import com.squareup.javapoet.MethodSpec.Builder;
import ru.art.core.caster.Caster;
import ru.art.core.checker.CheckerForEmptiness;
import ru.art.entity.PrimitiveMapping;
import ru.art.generator.spec.common.annotation.*;
import ru.art.generator.spec.common.constants.SpecificationType;
import ru.art.generator.spec.common.exception.MethodConsumesWithoutParamsException;
import ru.art.generator.spec.common.exception.SpecAnnotationDefinitionException;
import ru.art.generator.spec.common.exception.SpecificationTypeDefinitionException;
import ru.art.generator.spec.http.common.annotation.*;
import ru.art.generator.spec.http.proxyspec.annotation.MethodPath;
import ru.art.generator.spec.http.proxyspec.constants.HttpProxySpecAnnotations;
import ru.art.generator.spec.http.proxyspec.model.HttpProxyMethodsAnnotations;
import ru.art.generator.spec.http.proxyspec.model.StaticImports;
import ru.art.http.client.communicator.HttpCommunicator;
import ru.art.http.client.interceptor.HttpClientInterceptor;
import ru.art.http.constants.MimeToContentTypeMapper;
import static java.io.File.separator;
import static java.text.MessageFormat.format;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.generator.common.constants.Constants.*;
import static ru.art.generator.common.operations.CommonOperations.isClassPrimitive;
import static ru.art.generator.common.operations.CommonOperations.printError;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.CAST;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.METHOD_NAME_STRING;
import static ru.art.generator.spec.common.constants.SpecificationType.httpProxySpec;
import static ru.art.generator.spec.common.operations.AnnotationsChecker.methodHasAnnotation;
import static ru.art.generator.spec.http.common.constants.HttpSpecConstants.Errors.METHOD_CONSUMES_WITHOUT_PARAMS;
import static ru.art.generator.spec.http.proxyspec.constants.HttpProxySpecAnnotations.*;
import static ru.art.generator.spec.http.proxyspec.constants.HttpProxySpecConstants.*;
import static ru.art.generator.spec.http.proxyspec.operations.HttpProxySpecificationClassGenerator.notGeneratedFieldsForMethod;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Interface contains operations, which helps in specification's parts generation.
 */
public interface HttpProxyAuxiliaryOperations {

    /**
     * Method adds statement to method builder, which describes
     * execution of certain service's method.
     *
     * @param currentMethod - method to be executed.
     * @param methodBuilder - builder for MethodSpec.
     * @param annotations   - model for annotations to check.
     * @param methodId      - id of current method.
     * @return methodBuilder with created line.
     */
    static Builder addMethodStatementForExecute(Method currentMethod, Builder methodBuilder, HttpProxyMethodsAnnotations annotations, String methodId) {
        if (annotations.isHasRequestMapper() && annotations.isHasResponseMapper())
            if (currentMethod.getParameters().length != 0) {
                if (!notGeneratedFieldsForMethod.get(currentMethod.getName()).containsValue(REQUEST_MAPPER)) {
                    methodBuilder.addStatement(EXEC_RESP_AND_REQ_PROXY_SPEC, methodId,
                            currentMethod.getAnnotation(MethodPath.class).methodPath().replace(separator, EMPTY_STRING),
                            currentMethod.getParameters()[0].getType());
                } else {
                    methodBuilder.addStatement(EXEC_RESP_PROXY_SPEC, methodId,
                            currentMethod.getAnnotation(MethodPath.class).methodPath().replace(separator, EMPTY_STRING));
                    methodBuilder.addStatement(TODO_NOT_GENERATED_REQ_IN_EXEC + DOUBLE_QUOTES + methodId + DOUBLE_QUOTES);
                }
            } else
                throw new MethodConsumesWithoutParamsException(format(METHOD_NAME_STRING, currentMethod.getName()) + METHOD_CONSUMES_WITHOUT_PARAMS);
        else if (annotations.isHasRequestMapper())
            if (currentMethod.getParameters().length != 0) {
                if (!notGeneratedFieldsForMethod.get(currentMethod.getName()).containsValue(REQUEST_MAPPER)) {
                    methodBuilder.addStatement(EXEC_REQ_PROXY_SPEC, methodId,
                            currentMethod.getAnnotation(MethodPath.class).methodPath().replace(separator, EMPTY_STRING),
                            currentMethod.getParameters()[0].getType());
                } else {
                    methodBuilder.addStatement(TODO_NOT_GENERATED_REQ_IN_EXEC + DOUBLE_QUOTES + methodId + DOUBLE_QUOTES);
                    methodBuilder.addStatement(EXEC_NO_RESP_OR_REQ_PROXY_SPEC, methodId,
                            currentMethod.getAnnotation(MethodPath.class).methodPath().replace(separator, EMPTY_STRING));
                }
            } else
                throw new MethodConsumesWithoutParamsException(format(METHOD_NAME_STRING, currentMethod.getName()) + METHOD_CONSUMES_WITHOUT_PARAMS);
        else
            methodBuilder.addStatement(annotations.isHasResponseMapper() ? EXEC_RESP_PROXY_SPEC : EXEC_NO_RESP_OR_REQ_PROXY_SPEC,
                    methodId,
                    currentMethod.getAnnotation(MethodPath.class).methodPath().replace(separator, EMPTY_STRING));
        return methodBuilder;
    }

    /**
     * Method defines which static imports are needed to be added
     * and adds it to javaFileBuilder.
     *
     * @param javaFileBuilder   - builder for java file.
     * @param importsForMethods - map with boolean values
     *                          to define imports witch need to be included based on each method of service.
     */
    static void addStaticImports(JavaFile.Builder javaFileBuilder, Map<Method, StaticImports> importsForMethods) {
        StaticImports importsNeeded = new StaticImports();
        for (Map.Entry<Method, StaticImports> entry : importsForMethods.entrySet()) {
            if (isNotEmpty(entry.getValue())) {
                if (!importsNeeded.isHasMethodWithResponse() && entry.getValue().isHasMethodWithResponse()) {
                    javaFileBuilder.addStaticImport(Caster.class, CAST);
                    javaFileBuilder.addStaticImport(CheckerForEmptiness.class, UNWRAP);
                    importsNeeded.setHasMethodWithResponse(true);
                }

                if (entry.getValue().isHasMappers()) {
                    if (methodHasAnnotation(entry.getKey(), RequestMapper.class, httpProxySpec)) {
                        Class reqMapper = Object.class.equals(entry.getKey().getAnnotation(RequestMapper.class).forAll())
                                ? entry.getKey().getAnnotation(RequestMapper.class).httpProxy()
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

                    if (methodHasAnnotation(entry.getKey(), ResponseMapper.class, httpProxySpec)) {
                        Class respMapper = Object.class.equals(entry.getKey().getAnnotation(ResponseMapper.class).forAll())
                                ? entry.getKey().getAnnotation(ResponseMapper.class).httpProxy()
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
                }

                if (!importsNeeded.isHasHttpClientProxyWithoutParams() && entry.getValue().isHasHttpClientProxyWithoutParams()) {
                    javaFileBuilder.addStaticImport(HttpCommunicator.class, WILDCARD);
                    importsNeeded.setHasHttpClientProxyWithoutParams(true);
                }

                if (!importsNeeded.isHasInterceptor() && entry.getValue().isHasInterceptor()) {
                    javaFileBuilder.addStaticImport(HttpClientInterceptor.class, WILDCARD);
                    importsNeeded.setHasInterceptor(true);
                }

                if (!importsNeeded.isHasPrimitiveMapper() && entry.getValue().isHasPrimitiveMapper()) {
                    javaFileBuilder.addStaticImport(PrimitiveMapping.class, WILDCARD);
                    importsNeeded.setHasPrimitiveMapper(true);
                }

                if (!importsNeeded.isHasMimeTypes() && entry.getValue().isHasMimeTypes()) {
                    javaFileBuilder.addStaticImport(MimeToContentTypeMapper.class, WILDCARD);
                    importsNeeded.setHasMimeTypes(true);
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
    static void checkHttpProxyAnnotations(Method currentMethod, SpecificationType specType, HttpProxyMethodsAnnotations annotations) {
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
            annotations.setHasDelete(methodHasAnnotation(currentMethod, HttpDelete.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasHead(methodHasAnnotation(currentMethod, HttpHead.class, specType));
        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
            printError(exception.getMessage());
        }
        try {
            annotations.setHasPut(methodHasAnnotation(currentMethod, HttpPut.class, specType));
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
            annotations.setHasTrace(methodHasAnnotation(currentMethod, HttpTrace.class, specType));
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
            annotations.setHasMethodPath(methodHasAnnotation(currentMethod, MethodPath.class, specType));
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
    }

    /**
     * Method gets codeBlock from generatedFields map by http method's annotation.
     * GeneratedFields map can't contain several http method's annotation.
     *
     * @param generatedFields - map of fields witch were generated in CodeBlock form.
     * @return Generated CodeBlock.
     */
    static CodeBlock getPresentHttpMethodsAnnotation(Map<HttpProxySpecAnnotations, CodeBlock> generatedFields) {
        return generatedFields.containsKey(HTTP_PROXY_POST) ? generatedFields.get(HTTP_PROXY_POST)
                : generatedFields.containsKey(HTTP_PROXY_GET) ? generatedFields.get(HTTP_PROXY_GET)
                : generatedFields.containsKey(HTTP_PROXY_CONNECT) ? generatedFields.get(HTTP_PROXY_CONNECT)
                : generatedFields.containsKey(HTTP_PROXY_DELETE) ? generatedFields.get(HTTP_PROXY_DELETE)
                : generatedFields.containsKey(HTTP_PROXY_HEAD) ? generatedFields.get(HTTP_PROXY_HEAD)
                : generatedFields.containsKey(HTTP_PROXY_PATCH) ? generatedFields.get(HTTP_PROXY_PATCH)
                : generatedFields.containsKey(HTTP_PROXY_OPTIONS) ? generatedFields.get(HTTP_PROXY_OPTIONS)
                : generatedFields.containsKey(HTTP_PROXY_PUT) ? generatedFields.get(HTTP_PROXY_PUT)
                : generatedFields.get(HTTP_PROXY_TRACE);
    }

    /**
     * Defines generated method's parametrized type.
     * Possible types: {@link HttpCommunicator}
     * {@link HttpCommunicator}
     * Possible combinations of generics:
     * <Req, ?> - when method has request mapper;
     * <?, Resp> - when method has response mapper;
     * <Req, Resp> - when method has both mappers;
     * <?, ?> - when method has no mappers.
     *
     * @param method          - current method to create.
     * @param generatedFields - map of fields witch were generated in CodeBlock form.
     * @return parametrized type for generated method.
     */
    static ParameterizedTypeName getParametrizedFieldType(Method method, Map<HttpProxySpecAnnotations, CodeBlock> generatedFields) {
        TypeName classOfAny = WildcardTypeName.subtypeOf(Object.class);
        return generatedFields.containsKey(REQUEST_MAPPER) && generatedFields.containsKey(RESPONSE_MAPPER)
                ? ParameterizedTypeName.get(HttpCommunicator.class,
                method.getAnnotation(RequestMapper.class).httpProxy(),
                method.getAnnotation(ResponseMapper.class).httpProxy())
                : generatedFields.containsKey(REQUEST_MAPPER) && !generatedFields.containsKey(RESPONSE_MAPPER)
                ? ParameterizedTypeName.get(ClassName.get(HttpCommunicator.class),
                ClassName.get(method.getAnnotation(RequestMapper.class).httpProxy()), classOfAny)
                : !generatedFields.containsKey(REQUEST_MAPPER) && generatedFields.containsKey(RESPONSE_MAPPER)
                ? ParameterizedTypeName.get(ClassName.get(HttpCommunicator.class),
                classOfAny, ClassName.get(method.getAnnotation(ResponseMapper.class).httpProxy()))
                : ParameterizedTypeName.get(ClassName.get(HttpCommunicator.class),
                classOfAny, classOfAny);
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
    static boolean annotationWithNoRequestWasGenerated(Map<HttpProxySpecAnnotations, CodeBlock> generatedFields) {
        return generatedFields.containsKey(HTTP_PROXY_TRACE)
                || generatedFields.containsKey(HTTP_PROXY_HEAD)
                || generatedFields.containsKey(HTTP_PROXY_CONNECT);
    }
}

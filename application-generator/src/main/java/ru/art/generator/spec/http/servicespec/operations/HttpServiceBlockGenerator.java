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

package ru.art.generator.spec.http.servicespec.operations;

import com.squareup.javapoet.CodeBlock;
import ru.art.generator.exception.NotSupportedTypeForPrimitiveMapperException;
import ru.art.generator.spec.common.annotation.*;
import ru.art.generator.spec.common.exception.SpecAnnotationDefinitionException;
import ru.art.generator.spec.common.exception.SpecificationTypeDefinitionException;
import ru.art.generator.spec.http.common.annotation.*;
import ru.art.generator.spec.http.common.exception.MimeTypeDefinitionException;
import ru.art.generator.spec.http.servicespec.annotation.FromMultipart;
import ru.art.generator.spec.http.servicespec.constants.HttpServiceSpecAnnotations;
import ru.art.generator.spec.http.servicespec.exception.HttpServiceSpecAnnotationIdentificationException;
import ru.art.generator.spec.http.servicespec.model.*;
import static java.text.MessageFormat.format;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.generator.common.constants.Constants.*;
import static ru.art.generator.common.operations.CommonOperations.*;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.AnnotationParametersToMethods.*;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.*;
import static ru.art.generator.spec.common.constants.SpecificationType.httpServiceSpec;
import static ru.art.generator.spec.common.mapping.MimeTypeToContentTypeMethodMapping.getMimeTypeToContentMethod;
import static ru.art.generator.spec.common.operations.AnnotationsChecker.methodHasAnnotation;
import static ru.art.generator.spec.common.operations.IdCalculator.getMethodId;
import static ru.art.generator.spec.http.common.constants.HttpSpecConstants.Errors.NO_HTTP_METHODS_ANNOTATION;
import static ru.art.generator.spec.http.common.constants.HttpSpecConstants.Errors.UNABLE_TO_GENERATE_BECAUSE_OF_ANNOTATION;
import static ru.art.generator.spec.http.common.constants.HttpSpecConstants.Methods.*;
import static ru.art.generator.spec.http.common.operations.HttpAnnotationsChecker.amountOfHttpMethodsAnnotations;
import static ru.art.generator.spec.http.servicespec.constants.HttpServiceSpecAnnotations.*;
import static ru.art.generator.spec.http.servicespec.constants.HttpServiceSpecConstants.ExceptionConstants.*;
import static ru.art.generator.spec.http.servicespec.constants.HttpServiceSpecConstants.Methods.*;
import static ru.art.generator.spec.http.servicespec.operations.HttpServiceAuxiliaryOperations.*;
import static ru.art.generator.spec.http.servicespec.operations.HttpServiceSpecificationClassGenerator.methodAnnotations;
import static ru.art.generator.spec.http.servicespec.operations.HttpServiceSpecificationClassGenerator.methodIds;
import static ru.art.service.constants.RequestValidationPolicy.NOT_NULL;
import static ru.art.service.constants.RequestValidationPolicy.VALIDATABLE;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface with methods to create httpService constants
 * and all additional methods.
 */
public interface HttpServiceBlockGenerator {

    /**
     * Method for getting all codeblocks for httpService constant
     * by parsed annotations if there is any http method annotation.
     * Http method annotations: @HttpPost, @HttpGet, @HttpDelete, @HttpHead,
     *
     * @param data - all data necessary to generate httpService constant.
     * @HttpOptions, @HttpPatch, @HttpPut, @HttpTrace, @HttpConnect.
     */
    static void generateHttpServiceConstant(HttpServiceConstantGenerationData data) {
        methodAnnotations.put(data.getBlockData().getMethod().getName(), data.getBlockData().getHasAnnotations());

        data.getBlockData().setFromAnnotation(data.getBlockData().getHasAnnotations().isHasFromBody() ? FROM_BODY :
                data.getBlockData().getHasAnnotations().isHasFromMultipart() ? FROM_MULTIPART :
                        data.getBlockData().getHasAnnotations().isHasFromPathParams() ? FROM_PATH_PARAMS :
                                data.getBlockData().getHasAnnotations().isHasFromQueryParams() ? FROM_QUERY_PARAMS : null);

        data.getBlockData().setValidationPolicy(data.getBlockData().getHasAnnotations().isHasValidatable()
                ? HttpServiceSpecAnnotations.VALIDATABLE : data.getBlockData().getHasAnnotations().isHasNotNull()
                ? HttpServiceSpecAnnotations.NOT_NULL : null);

        data.getBlockData().setGeneratedFields(new HashMap<>());
        if (amountOfHttpMethodsAnnotations(data.getBlockData().getHasAnnotations()) == 0) {
            printError(format(NO_HTTP_METHODS_ANNOTATION, data.getBlockData().getMethod().getName()));
            data.getNotGeneratedFields().put(data.getBlockData().getMethod().getName(), null);
            return;
        }
        parseAnnotations(data.getBlockData(), data.getNotGeneratedFields());
        fillCodeBlock(data.getFillData(), data.getBlockData());
    }

    /**
     * Method parse method's annotations and fill generatedFields map
     * with annotation's class name and corresponding codeblock.
     *
     * @param data               - all data necessary to define codeblock for each method's annotation.
     * @param notGeneratedFields - map of fields which wasn't generated for methods.
     */
    static void parseAnnotations(HttpServiceBlockData data, Map<String, HttpServiceSpecAnnotations> notGeneratedFields) {
        for (Annotation annotation : data.getMethod().getDeclaredAnnotations()) {
            try {
                HttpServiceSpecAnnotations currentAnnotation = parseAnnotationClassName(annotation.annotationType().getSimpleName());
                String MethodNameForError = format(METHOD_NAME_STRING, data.getMethod().getName());
                switch (parseAnnotationClassName(annotation.annotationType().getSimpleName())) {
                    case HTTP_POST:
                    case HTTP_GET:
                    case HTTP_DELETE:
                    case HTTP_HEAD:
                    case HTTP_OPTIONS:
                    case HTTP_PATCH:
                    case HTTP_PUT:
                    case HTTP_TRACE:
                    case HTTP_CONNECT:
                        try {
                            if (methodHasAnnotation(data.getMethod(), annotation.annotationType(), httpServiceSpec)) {
                                String pattern = getBlockPatternForHttpMethod(currentAnnotation);
                                methodIds.add(HTTP_POST.getClassName().equals(currentAnnotation.getClassName())
                                        ? getMethodId(data.getMethod().getAnnotation(HttpPost.class).httpService())
                                        : HTTP_GET.getClassName().equals(currentAnnotation.getClassName())
                                        ? getMethodId(data.getMethod().getAnnotation(HttpGet.class).httpService())
                                        : HTTP_CONNECT.getClassName().equals(currentAnnotation.getClassName())
                                        ? getMethodId(data.getMethod().getAnnotation(HttpConnect.class).httpService())
                                        : HTTP_DELETE.getClassName().equals(currentAnnotation.getClassName())
                                        ? getMethodId(data.getMethod().getAnnotation(HttpDelete.class).httpService())
                                        : HTTP_HEAD.getClassName().equals(currentAnnotation.getClassName())
                                        ? getMethodId(data.getMethod().getAnnotation(HttpHead.class).httpService())
                                        : HTTP_OPTIONS.getClassName().equals(currentAnnotation.getClassName())
                                        ? getMethodId(data.getMethod().getAnnotation(HttpOptions.class).httpService())
                                        : HTTP_PATCH.getClassName().equals(currentAnnotation.getClassName())
                                        ? getMethodId(data.getMethod().getAnnotation(HttpPatch.class).httpService())
                                        : HTTP_TRACE.getClassName().equals(currentAnnotation.getClassName())
                                        ? getMethodId(data.getMethod().getAnnotation(HttpTrace.class).httpService())
                                        : getMethodId(data.getMethod().getAnnotation(HttpPut.class).httpService()));
                                data.getGeneratedFields().put(currentAnnotation, getBuilderLineForField(pattern, methodIds.get(methodIds.size() - 1)));
                            }
                        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException e) {
                            printError(e.getMessage());
                        }
                        break;
                    case HTTP_CONSUMES:
                    case HTTP_PRODUCES:
                        try {
                            if (methodHasAnnotation(data.getMethod(), annotation.annotationType(), httpServiceSpec)) {
                                data.getGeneratedFields().put(currentAnnotation, currentAnnotation.getClassName().equals(HTTP_CONSUMES.getClassName())
                                        ? getBuilderLineForField(CONSUMES, getMimeTypeToContentMethod(((Consumes) annotation).httpService()))
                                        : getBuilderLineForField(PRODUCES, getMimeTypeToContentMethod(((Produces) annotation).httpService())));
                                data.getImports().setHasMimeTypes(true);
                            }
                        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException | MimeTypeDefinitionException exception) {
                            printError(exception.getMessage());
                        }
                        break;
                    case REQUEST_MAPPER:
                        if (data.getHasAnnotations().isHasRequestMapper()) {
                            CodeBlock request;
                            Class fromClass = Object.class;
                            Class validationClass = Object.class;
                            CodeBlock from = formFromLine(data.getMethod(), notGeneratedFields, data.getHasAnnotations());
                            if (isNotEmpty(from)) {
                                fromClass = data.getHasAnnotations().isHasFromBody() ? FromBody.class :
                                        data.getHasAnnotations().isHasFromMultipart() ? FromMultipart.class :
                                                data.getHasAnnotations().isHasFromPathParams() ? FromPathParams.class :
                                                        FromQueryParams.class;
                            }

                            CodeBlock validation = formValidatableLine(data.getMethod(), notGeneratedFields, data.getImports(), data.getHasAnnotations());
                            if (isNotEmpty(validation)) {
                                validationClass = data.getHasAnnotations().isHasValidatable() ? Validatable.class : NotNull.class;
                            }
                            try {
                                request = formRequestMapperLine(data.getMethod(), data.getImports());
                                if (isNotEmpty(from)) {
                                    data.getGeneratedFields().put(REQUEST_MAPPER, request);
                                    data.getGeneratedFields().put(data.getFromAnnotation(), from);
                                    data.getImports().setHasServiceMethods(true);
                                } else {
                                    printError(MethodNameForError + PARAM_FROM_IS_MISSING);
                                    notGeneratedFields.put(data.getMethod().getName() + COLON + RequestMapper.class.getSimpleName(), REQUEST_MAPPER);
                                    if (isNotEmpty(validation)) {
                                        notGeneratedFields.put(data.getMethod().getName() + COLON + validationClass.getSimpleName(), data.getValidationPolicy());
                                    }
                                }
                                if (isNotEmpty(validation)) {
                                    data.getGeneratedFields().put(data.getValidationPolicy(), validation);
                                    data.getImports().setHasValidationPolicy(true);
                                }
                            } catch (HttpServiceSpecAnnotationIdentificationException exception) {
                                printError(MethodNameForError + exception.getMessage());
                                notGeneratedFields.put(data.getMethod().getName() + COLON + RequestMapper.class.getSimpleName(), REQUEST_MAPPER);
                                if (isEmpty(from))
                                    printError(MethodNameForError + PARAM_FROM_IS_MISSING);
                                else {
                                    notGeneratedFields.put(data.getMethod().getName() + COLON + fromClass.getSimpleName(), data.getFromAnnotation());
                                    data.getGeneratedFields().remove(data.getFromAnnotation());
                                    printError(MethodNameForError +
                                            format(PARAM_FROM_NOT_GENERATED_CAUSE_OF_VALIDATION, data.getHasAnnotations().isHasFromBody() ? FROM_BODY_METHOD :
                                                    data.getHasAnnotations().isHasFromMultipart() ? FROM_MULTIPART_METHOD :
                                                            data.getHasAnnotations().isHasFromPathParams() ? FROM_PATH_PARAMS_METHOD :
                                                                    FROM_QUERY_PARAMS_METHOD));
                                }
                                if (isNotEmpty(validation)) {
                                    notGeneratedFields.put(data.getMethod().getName() + COLON + validationClass.getSimpleName(), data.getValidationPolicy());
                                    data.getGeneratedFields().remove(data.getValidationPolicy());
                                    printError(MethodNameForError +
                                            format(PARAM_FROM_NOT_GENERATED_CAUSE_OF_VALIDATION, VALIDATION_POLICY_METHOD));
                                }
                                data.getImports().setHasValidationPolicy(false);
                            }
                        }
                        break;
                    case FROM_BODY:
                    case FROM_MULTIPART:
                    case FROM_PATH_PARAMS:
                    case FROM_QUERY_PARAMS:
                        try {
                            if (methodHasAnnotation(data.getMethod(), annotation.annotationType(), httpServiceSpec)) {
                                if (!data.getHasAnnotations().isHasRequestMapper()) {
                                    printError(MethodNameForError +
                                            format(PARAM_REQUEST_IS_MISSING, currentAnnotation.equals(FROM_BODY)
                                                    ? FROM_BODY_METHOD
                                                    : currentAnnotation.equals(FROM_MULTIPART)
                                                    ? FROM_MULTIPART_METHOD
                                                    : currentAnnotation.equals(FROM_PATH_PARAMS)
                                                    ? FROM_PATH_PARAMS_METHOD : FROM_QUERY_PARAMS_METHOD));
                                    notGeneratedFields.put(data.getMethod().getName() + COLON + currentAnnotation.getClassName(), currentAnnotation);
                                }
                            }
                        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException e) {
                            printError(e.getMessage());
                        }
                        break;
                    case RESPONSE_MAPPER:
                        if (data.getHasAnnotations().isHasResponseMapper()) {
                            data.getGeneratedFields().put(RESPONSE_MAPPER, formResponseMapperLine(data.getMethod(), data.getImports()));
                            data.getImports().setHasServiceMethods(true);
                            data.getImports().setHasCaster(true);
                        }
                        break;
                    case REQUEST_INTERCEPTOR:
                        if (data.getHasAnnotations().isHasReqInterceptor()) {
                            data.getGeneratedFields().put(currentAnnotation,
                                    getBuilderLineForField(ADD_REQUEST_INTERCEPTOR,
                                            INTERCEPT_REQUEST,
                                            Object.class.equals(((RequestInterceptor) annotation).forAll())
                                                    ? ((RequestInterceptor) annotation).httpService()
                                                    : ((RequestInterceptor) annotation).forAll()));
                            data.getImports().setHasInterceptor(true);
                        }
                        break;
                    case RESPONSE_INTERCEPTOR:
                        if (data.getHasAnnotations().isHasRespInterceptor()) {
                            data.getGeneratedFields().put(currentAnnotation,
                                    getBuilderLineForField(ADD_RESPONSE_INTERCEPTOR,
                                            INTERCEPT_RESPONSE,
                                            Object.class.equals(((ResponseInterceptor) annotation).forAll())
                                                    ? ((ResponseInterceptor) annotation).httpService()
                                                    : ((ResponseInterceptor) annotation).forAll()));
                            data.getImports().setHasInterceptor(true);
                        }
                        break;
                    case VALIDATABLE:
                    case NOT_NULL:
                        try {
                            if (methodHasAnnotation(data.getMethod(), annotation.annotationType(), httpServiceSpec)) {
                                if (!data.getHasAnnotations().isHasRequestMapper()) {
                                    printError(MethodNameForError +
                                            format(PARAM_REQUEST_IS_MISSING, VALIDATION_POLICY_METHOD));
                                    notGeneratedFields.put(data.getMethod().getName() + COLON + currentAnnotation.getClassName(), currentAnnotation);
                                }
                            }
                        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException e) {
                            printError(e.getMessage());
                        }
                        break;
                }
            } catch (HttpServiceSpecAnnotationIdentificationException exception) {
                printError(exception.getMessage());
            }
        }
    }

    /**
     * Generates line for "withResp" block for certain method.
     *
     * @param method - current method.
     * @return - CodeBlock containing line for response mapper.
     * - null if annotation @ResponseMapper is not present.
     */
    static CodeBlock formResponseMapperLine(Method method, StaticImports imports) {
        Class respMapperAnnotationClass = Object.class.equals(method.getAnnotation(ResponseMapper.class).forAll())
                ? method.getAnnotation(ResponseMapper.class).httpService()
                : method.getAnnotation(ResponseMapper.class).forAll();
        String respMapperAnnotationClassName = respMapperAnnotationClass.getSimpleName();

        try {
            String primitiveMapper = getFromModelMappingFromType(respMapperAnnotationClass);
            imports.setHasPrimitiveMapper(true);
            return getBuilderLineForField(RESP_MAPPER, primitiveMapper);
        } catch (NotSupportedTypeForPrimitiveMapperException e) {
            return getBuilderLineForField(RESP_MAPPER, FROM_MODEL +
                    respMapperAnnotationClassName
                            .replace(RESPONSE, EMPTY_STRING)
                            .replace(XML_MAPPER, EMPTY_STRING)
                            .replace(MAPPER, EMPTY_STRING));
        }
    }

    /**
     * Generates line for "requestMapper" block for certain method.
     *
     * @param method  - current method.
     * @param imports - model with variables to mark if certain import is needed.
     * @return - CodeBlock containing line for request mapper.
     * @throws HttpServiceSpecAnnotationIdentificationException is thrown when it'a impossible to define exact validation annotation.
     */
    static CodeBlock formRequestMapperLine(Method method, StaticImports imports) throws HttpServiceSpecAnnotationIdentificationException {
        Class reqMapperAnnotationClass = Object.class.equals(method.getAnnotation(RequestMapper.class).forAll())
                ? method.getAnnotation(RequestMapper.class).httpService()
                : method.getAnnotation(RequestMapper.class).forAll();
        String reqMapperAnnotationClassName = reqMapperAnnotationClass.getSimpleName();

        try {
            String primitiveMapper = getToModelMappingFromType(reqMapperAnnotationClass);
            imports.setHasPrimitiveMapper(true);
            return getBuilderLineForField(REQ_MAPPER, primitiveMapper);
        } catch (NotSupportedTypeForPrimitiveMapperException e) {
            return getBuilderLineForField(REQ_MAPPER, TO_MODEL +
                    reqMapperAnnotationClassName
                            .replace(REQUEST, EMPTY_STRING)
                            .replace(XML_MAPPER, EMPTY_STRING)
                            .replace(MAPPER, EMPTY_STRING));
        }
    }

    /**
     * Generates line for "validationPolicy" block for certain method.
     *
     * @param method  - current method.
     * @param imports - model with variables to mark if certain import is needed.
     * @return - CodeBlock containing line for validation policy.
     * - null if annotation @Validatable and @NotNull are not present or
     * there are both of them.
     * @throws HttpServiceSpecAnnotationIdentificationException is thrown when it'a impossible to define exact validation annotation.
     */
    static CodeBlock formValidatableLine(Method method, Map<String, HttpServiceSpecAnnotations> notGeneratedFields, StaticImports imports, HttpServiceMethodsAnnotations annotations) {
        if (annotations.isHasValidatable() && annotations.isHasNotNull()) {
            notGeneratedFields.put(method.getName() + COLON + Validatable.class.getSimpleName(), HttpServiceSpecAnnotations.VALIDATABLE);
            if (annotations.isHasRequestMapper()) {
                notGeneratedFields.put(method.getName() + COLON + RequestMapper.class.getSimpleName(), REQUEST_MAPPER);
            }
            throw new HttpServiceSpecAnnotationIdentificationException(UNABLE_TO_DEFINE_VALIDATION_ANNOTATION);
        }
        imports.setHasValidationPolicy(annotations.isHasValidatable() || annotations.isHasNotNull());
        return annotations.isHasValidatable() || annotations.isHasNotNull() ?
                getBuilderLineForField(VALIDATION_POLICY, annotations.isHasValidatable() ? VALIDATABLE.toString() : NOT_NULL.toString())
                : null;
    }

    /**
     * Generates line for "fromBody"/"fromMultipart"/"fromPathParams"/"fromQueryParams"
     * block for certain method based on annotations.
     *
     * @param method             - current method.
     * @param notGeneratedFields - map of fields which wasn't generated for methods.
     * @param annotations        - model for annotations to check.
     * @return - CodeBlock containing line with "from..." part.
     * - null if neither of @FromBody, @FromMultipart, @FromPathParams, @FromQueryParams
     * annotations are present.
     */
    static CodeBlock formFromLine(Method method, Map<String, HttpServiceSpecAnnotations> notGeneratedFields, HttpServiceMethodsAnnotations annotations) {
        if (annotations.isHasFromBody()) {
            if (!annotations.isHasFromMultipart() && !annotations.isHasFromPathParams() && !annotations.isHasFromQueryParams())
                return getBuilderLineForField(FROM_BODY_METHOD, EMPTY_STRING);
            else notGeneratedFields.put(method.getName() + COLON + FromBody.class.getSimpleName(), FROM_BODY);
            return null;
        }

        if (annotations.isHasFromMultipart()) {
            if (!annotations.isHasFromPathParams() && !annotations.isHasFromQueryParams())
                return getBuilderLineForField(FROM_MULTIPART_METHOD, EMPTY_STRING);
            else notGeneratedFields.put(method.getName() + COLON + FromMultipart.class.getSimpleName(), FROM_MULTIPART);
            return null;
        }

        if (annotations.isHasFromPathParams()) {
            if (!annotations.isHasFromQueryParams())
                return getBuilderLineForField(FROM_PATH_PARAMS_METHOD, EMPTY_STRING);
            else
                notGeneratedFields.put(method.getName() + COLON + FromPathParams.class.getSimpleName(), FROM_PATH_PARAMS);
            return null;
        }

        if (annotations.isHasFromQueryParams())
            return getBuilderLineForField(FROM_QUERY_PARAMS_METHOD, EMPTY_STRING);

        return null;
    }

    /**
     * Method fill array of codeBlocks for httpService
     * based on generated fields.
     *
     * @param fillData - all data to be filled.
     * @param data     - all data to be based on during filling array of codeBlocks.
     */
    static void fillCodeBlock(HttpServiceCodeBlockFillData fillData, HttpServiceBlockData data) {
        if (data.getGeneratedFields().containsKey(REQUEST_INTERCEPTOR))
            fillData.getCodeBlocks().add(data.getGeneratedFields().get(REQUEST_INTERCEPTOR));

        if (data.getGeneratedFields().containsKey(RESPONSE_INTERCEPTOR))
            fillData.getCodeBlocks().add(data.getGeneratedFields().get(RESPONSE_INTERCEPTOR));
        fillData.getCodeBlocks().add(generatedFieldsHttpMethodValue(data.getGeneratedFields()));

        if (!annotationWithNoRequestWasGenerated(data.getGeneratedFields())) {
            if (data.getGeneratedFields().containsKey(HTTP_CONSUMES))
                fillData.getCodeBlocks().add(data.getGeneratedFields().get(HTTP_CONSUMES));
            if (data.getGeneratedFields().containsKey(REQUEST_MAPPER)) {
                fillData.getCodeBlocks().add(data.getGeneratedFields().get(data.getFromAnnotation()));
                fillData.getCodeBlocks().add(data.getGeneratedFields().get(data.getValidationPolicy()));
                fillData.getCodeBlocks().add(data.getGeneratedFields().get(REQUEST_MAPPER));
            }
        } else {
            StringBuilder fieldsForError = new StringBuilder();
            if (data.getGeneratedFields().containsKey(REQUEST_MAPPER))
                fieldsForError.append(format(ANNOTATION, REQUEST_MAPPER.getClassName()));
            if (data.getGeneratedFields().containsKey(HTTP_CONSUMES)) {
                if (!isEmpty(fieldsForError.toString())) fieldsForError.append(COMMA);
                fieldsForError.append(format(ANNOTATION, HTTP_CONSUMES.getClassName()));
            }

            printError(format(METHOD_NAME_STRING, data.getMethod().getName())
                    + format(UNABLE_TO_GENERATE_BECAUSE_OF_ANNOTATION,
                    fieldsForError.toString(),
                    data.getGeneratedFields().containsKey(HTTP_TRACE) ? HTTP_TRACE.getClassName()
                            : data.getGeneratedFields().containsKey(HTTP_HEAD) ? HTTP_HEAD.getClassName()
                            : HTTP_CONNECT.getClassName()));
            data.getGeneratedFields().remove(HTTP_CONSUMES);
            data.getGeneratedFields().remove(REQUEST_MAPPER);
        }

        if (data.getGeneratedFields().containsKey(HTTP_PRODUCES)) {
            fillData.getCodeBlocks().add(data.getGeneratedFields().get(HTTP_PRODUCES));
        }

        if (data.getGeneratedFields().containsKey(RESPONSE_MAPPER)) {
            fillData.getCodeBlocks().add(data.getGeneratedFields().get(RESPONSE_MAPPER));
        }

        if (!data.getHasAnnotations().isHasListen())
            fillData.setDefaultListenCount(fillData.getDefaultListenCount() + 1);
        fillData.getCodeBlocks().add(getBuilderLineForField(data.getHasAnnotations().isHasListen() ? LISTEN_METHOD_FILLED : LISTEN_METHOD,
                data.getHasAnnotations().isHasListen() ? data.getMethod().getAnnotation(Listen.class).httpServiceMethodPath() : EMPTY_STRING));
        fillData.getImportsForMethods().put(data.getMethod(), data.getImports());
    }
}

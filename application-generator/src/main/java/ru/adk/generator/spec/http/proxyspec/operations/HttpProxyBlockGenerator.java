package ru.adk.generator.spec.http.proxyspec.operations;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import lombok.Getter;
import lombok.experimental.Accessors;
import ru.adk.generator.common.annotation.GenerationException;
import ru.adk.generator.exception.NotSupportedTypeForPrimitiveMapperException;
import ru.adk.generator.spec.common.annotation.*;
import ru.adk.generator.spec.common.exception.SpecAnnotationDefinitionException;
import ru.adk.generator.spec.common.exception.SpecificationTypeDefinitionException;
import ru.adk.generator.spec.http.common.annotation.FromBody;
import ru.adk.generator.spec.http.common.annotation.FromPathParams;
import ru.adk.generator.spec.http.common.annotation.FromQueryParams;
import ru.adk.generator.spec.http.common.exception.MimeTypeDefinitionException;
import ru.adk.generator.spec.http.proxyspec.annotation.MethodPath;
import ru.adk.generator.spec.http.proxyspec.constants.HttpProxySpecAnnotations;
import ru.adk.generator.spec.http.proxyspec.exception.HttpProxySpecAnnotationDefinitionException;
import ru.adk.generator.spec.http.proxyspec.model.HttpProxyBlockData;
import ru.adk.generator.spec.http.proxyspec.model.HttpProxyCodeBlockFillData;
import ru.adk.generator.spec.http.proxyspec.model.HttpProxyMethodsAnnotations;
import ru.adk.generator.spec.http.proxyspec.model.StaticImports;
import static com.squareup.javapoet.CodeBlock.of;
import static java.text.MessageFormat.format;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.StringConstants.*;
import static ru.adk.generator.common.constants.Constants.*;
import static ru.adk.generator.common.constants.Constants.SymbolsAndFormatting.*;
import static ru.adk.generator.common.operations.CommonOperations.*;
import static ru.adk.generator.spec.common.constants.CommonSpecGeneratorConstants.AnnotationParametersToMethods.*;
import static ru.adk.generator.spec.common.constants.CommonSpecGeneratorConstants.*;
import static ru.adk.generator.spec.common.constants.SpecificationType.httpProxySpec;
import static ru.adk.generator.spec.common.mapping.MimeTypeToContentTypeMethodMapping.getMimeTypeToContentMethod;
import static ru.adk.generator.spec.common.operations.AnnotationsChecker.methodHasAnnotation;
import static ru.adk.generator.spec.common.operations.IdCalculator.getMethodId;
import static ru.adk.generator.spec.http.common.constants.HttpSpecConstants.Errors.*;
import static ru.adk.generator.spec.http.common.constants.HttpSpecConstants.Methods.*;
import static ru.adk.generator.spec.http.common.operations.HttpAnnotationsChecker.*;
import static ru.adk.generator.spec.http.proxyspec.constants.HttpProxySpecAnnotations.*;
import static ru.adk.generator.spec.http.proxyspec.constants.HttpProxySpecConstants.ErrorConstants.METHOD_PATH_IS_EMPTY;
import static ru.adk.generator.spec.http.proxyspec.constants.HttpProxySpecConstants.ErrorConstants.PARAM_FROM_IS_MISSING;
import static ru.adk.generator.spec.http.proxyspec.constants.HttpProxySpecConstants.HTTP_CLIENT_PROXY_BY_URL_WITHOUT_PARAMS;
import static ru.adk.generator.spec.http.proxyspec.constants.HttpProxySpecConstants.HTTP_CLIENT_PROXY_BY_URL_WITH_PARAMS;
import static ru.adk.generator.spec.http.proxyspec.operations.HttpProxyAuxiliaryOperations.*;
import static ru.adk.generator.spec.http.proxyspec.operations.HttpProxySpecificationClassGenerator.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Interface with methods to create block of method's constants
 * and all additional methods.
 */
public interface HttpProxyBlockGenerator {
    /**
     * Generate constant for certain method of service by annotations
     * if there is only one http method annotation and filled method path.
     * Http method annotations: @HttpPost, @HttpGet, @HttpDelete, @HttpHead,
     *
     * @param method            - generated method.
     * @param importsForMethods - map with boolean values to define imports to include.
     * @return FieldSpec containing constant.
     * @HttpOptions, @HttpPatch, @HttpPut, @HttpTrace, @HttpConnect.
     */
    static FieldSpec generateCurrentMethodBlock(Method method, Map<Method, StaticImports> importsForMethods) {
        CodeBlock.Builder codeBlocksBuilder = CodeBlock.builder();
        Map<String, HttpProxySpecAnnotations> notGeneratedFields = new LinkedHashMap<>();
        StaticImports imports = new StaticImports();
        HttpProxyMethodsAnnotations hasAnnotations = new HttpProxyMethodsAnnotations();
        checkHttpProxyAnnotations(method, httpProxySpec, hasAnnotations);
        if (!hasAnnotations.isHasMethodPath()) {
            printError(format(METHOD_NAME_STRING, method.getName()) + METHOD_PATH_IS_EMPTY);
            notGeneratedFields.put(method.getName(), null);
            return null;
        }
        if (serviceMethodHasSeveralHttpMethodsAnnotations(hasAnnotations)) {
            printError(format(METHOD_NAME_STRING, method.getName()) +
                    format(INCOMPATIBLE_ANNOTATIONS, method.getName(),
                            getIncompatibleHttpMethodsAnnotationsForServiceMethod(hasAnnotations)));
            notGeneratedFields.put(method.getName(), null);
            return null;
        }
        methodAnnotations.put(method.getName(), hasAnnotations);
        Map<HttpProxySpecAnnotations, CodeBlock> generatedFields = new HashMap<>();

        HttpProxyBlockData blockData = HttpProxyBlockData.builder()
                .generatedFields(generatedFields)
                .hasAnnotations(hasAnnotations)
                .imports(imports)
                .method(method)
                .reqMapper(Object.class)
                .respMapper(Object.class)
                .build();
        parseAnnotations(blockData, notGeneratedFields);

        if (generatedFields.size() == 0) return null;
        if (amountOfHttpMethodsAnnotations(hasAnnotations) == 0) {
            printError(format(NO_HTTP_METHODS_ANNOTATION, method.getName()));
            notGeneratedFields.put(method.getName(), null);
            return null;
        }

        fillCodeBlockHttpProxyBlockData(HttpProxyCodeBlockFillData.builder()
                .blockData(blockData)
                .codeBlocksBuilder(codeBlocksBuilder)
                .build());

        importsForMethods.put(method, imports);
        methodIds.add(getMethodId(method.getAnnotation(MethodPath.class).methodPath().replace(SLASH, EMPTY_STRING)));

        ParameterizedTypeName fieldType = getParametrizedFieldType(method, generatedFields);

        FieldSpec.Builder fieldBuilder = FieldSpec.builder(fieldType,
                method.getAnnotation(MethodPath.class).methodPath().replace(SLASH, EMPTY_STRING), PRIVATE, FINAL)
                .initializer(codeBlocksBuilder.build())
                .addAnnotation(AnnotationSpec.builder(Getter.class)
                        .addMember(LAZY, VALUE_PATTERN, true)
                        .build())
                .addAnnotation(AnnotationSpec.builder(Accessors.class)
                        .addMember(FLUENT, VALUE_PATTERN, true)
                        .build());
        notGeneratedFieldsForMethod.put(method.getName(), notGeneratedFields);
        return (notGeneratedFields.size() != 0)
                ? fieldBuilder.addAnnotation(AnnotationSpec.builder(GenerationException.class)
                .addMember(NOT_GENERATED_FIELDS, STRING_PATTERN, notGeneratedFields.keySet()).build())
                .build()
                : fieldBuilder.build();
    }

    /**
     * Method parse method's annotations and fill generatedFields map
     * with annotation's class name and corresponding codeblock.
     *
     * @param data               - all data necessary to define codeblock for each method's annotation.
     * @param notGeneratedFields - map of fields which wasn't generated for methods.
     */
    static void parseAnnotations(HttpProxyBlockData data, Map<String, HttpProxySpecAnnotations> notGeneratedFields) {
        for (Annotation annotation : data.getMethod().getDeclaredAnnotations()) {
            try {
                HttpProxySpecAnnotations currentAnnotation = parseAnnotationClassName(annotation.annotationType().getSimpleName());
                switch (currentAnnotation) {
                    case HTTP_PROXY_POST:
                    case HTTP_PROXY_GET:
                    case HTTP_PROXY_DELETE:
                    case HTTP_PROXY_HEAD:
                    case HTTP_PROXY_OPTIONS:
                    case HTTP_PROXY_PATCH:
                    case HTTP_PROXY_PUT:
                    case HTTP_PROXY_TRACE:
                    case HTTP_PROXY_CONNECT:
                        try {
                            if (methodHasAnnotation(data.getMethod(), annotation.annotationType(), httpProxySpec)) {
                                String pattern = currentAnnotation.getClassName().equals(HTTP_PROXY_POST.getClassName()) ? POST
                                        : currentAnnotation.getClassName().equals(HTTP_PROXY_GET.getClassName()) ? GET
                                        : currentAnnotation.getClassName().equals(HTTP_PROXY_DELETE.getClassName()) ? DELETE
                                        : currentAnnotation.getClassName().equals(HTTP_PROXY_HEAD.getClassName()) ? HEAD
                                        : currentAnnotation.getClassName().equals(HTTP_PROXY_OPTIONS.getClassName()) ? OPTIONS
                                        : currentAnnotation.getClassName().equals(HTTP_PROXY_PATCH.getClassName()) ? PATCH
                                        : currentAnnotation.getClassName().equals(HTTP_PROXY_PUT.getClassName()) ? PUT
                                        : currentAnnotation.getClassName().equals(HTTP_PROXY_TRACE.getClassName()) ? TRACE
                                        : CONNECT;
                                data.getGeneratedFields().put(currentAnnotation, getBuilderLineForField(pattern, EMPTY_STRING));
                            }
                        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException exception) {
                            printError(exception.getMessage());
                        }
                        break;
                    case HTTP_PROXY_CONSUMES:
                    case HTTP_PROXY_PRODUCES:
                        try {
                            if (methodHasAnnotation(data.getMethod(), annotation.annotationType(), httpProxySpec)) {
                                data.getGeneratedFields().put(currentAnnotation, currentAnnotation.getClassName().equals(HTTP_PROXY_CONSUMES.getClassName())
                                        ? getBuilderLineForField(CONSUMES, getMimeTypeToContentMethod(((Consumes) annotation).httpProxy()))
                                        : getBuilderLineForField(PRODUCES, getMimeTypeToContentMethod(((Produces) annotation).httpProxy())));
                                data.getImports().setHasMimeTypes(true);
                            }
                        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException | MimeTypeDefinitionException exception) {
                            printError(exception.getMessage());
                        }
                        break;
                    case REQUEST_MAPPER:
                        if (data.getHasAnnotations().isHasRequestMapper()) {
                            if (data.getHasAnnotations().isHasFromBody()) {
                                data.setReqMapper(Object.class.equals(((RequestMapper) annotation).forAll())
                                        ? ((RequestMapper) annotation).httpProxy()
                                        : ((RequestMapper) annotation).forAll());
                                data.getGeneratedFields().put(currentAnnotation, formRequestMapperLine(data.getMethod(), data.getImports()));
                                data.getImports().setHasMappers(true);
                            } else {
                                notGeneratedFields.put(data.getMethod().getName() + COLON + RequestMapper.class.getSimpleName(), REQUEST_MAPPER);
                                printError(format(METHOD_NAME_STRING, data.getMethod().getName()) + PARAM_FROM_IS_MISSING);
                            }
                        }
                        break;
                    case FROM_BODY:
                        if (data.getHasAnnotations().isHasFromBody()) {
                            if (!data.getHasAnnotations().isHasRequestMapper()) {
                                printError(format(METHOD_NAME_STRING, data.getMethod().getName()) +
                                        format(PARAM_IS_MISSING, FROM_BODY_METHOD, RequestMapper.class.getSimpleName()));
                                notGeneratedFields.put(data.getMethod().getName() + COLON + FromBody.class.getSimpleName(), FROM_BODY);
                            }
                        }
                        break;
                    case FROM_PATH_PARAMS:
                    case FROM_QUERY_PARAMS:
                        try {
                            if (methodHasAnnotation(data.getMethod(), annotation.annotationType(), httpProxySpec)) {
                                CodeBlock from = currentAnnotation.getClassName().equals(FROM_PATH_PARAMS.getClassName()) ?
                                        getBuilderLineForField(WITH_PATH_PARAMETER,
                                                ((FromPathParams) annotation).httpProxy())
                                        : getBuilderLineForField(WITH_QUERY_PARAMETER,
                                        ((FromQueryParams) annotation).httpProxyQueryName(),
                                        ((FromQueryParams) annotation).httpProxyQueryValue());
                                data.getGeneratedFields().put(currentAnnotation, from);
                            }
                        } catch (SpecificationTypeDefinitionException | SpecAnnotationDefinitionException | MimeTypeDefinitionException exception) {
                            printError(exception.getMessage());
                        }
                        break;
                    case RESPONSE_MAPPER:
                        if (data.getHasAnnotations().isHasResponseMapper()) {
                            data.setRespMapper(Object.class.equals(((ResponseMapper) annotation).forAll())
                                    ? ((ResponseMapper) annotation).httpProxy()
                                    : ((ResponseMapper) annotation).forAll());
                            data.getGeneratedFields().put(currentAnnotation, formResponseMapperLine(data.getMethod(), data.getImports()));
                            data.getImports().setHasMappers(true);
                            data.getImports().setHasMethodWithResponse(true);
                        }
                        break;
                    case REQUEST_INTERCEPTOR:
                        if (data.getHasAnnotations().isHasReqInterceptor()) {
                            data.getGeneratedFields().put(currentAnnotation,
                                    getBuilderLineForField(ADD_REQUEST_INTERCEPTOR,
                                            INTERCEPT_REQUEST,
                                            Object.class.equals(((RequestInterceptor) annotation).forAll())
                                                    ? ((RequestInterceptor) annotation).httpProxy()
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
                                                    ? ((ResponseInterceptor) annotation).httpProxy()
                                                    : ((ResponseInterceptor) annotation).forAll()));
                            data.getImports().setHasInterceptor(true);
                        }
                        break;
                }
            } catch (HttpProxySpecAnnotationDefinitionException e) {
                printError(format(METHOD_NAME_STRING, data.getMethod().getName()) + e.getMessage());
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
                ? method.getAnnotation(ResponseMapper.class).httpProxy()
                : method.getAnnotation(ResponseMapper.class).forAll();
        String respMapperAnnotationClassName = respMapperAnnotationClass.getSimpleName();
        try {
            String primitiveMapper = getToModelMappingFromType(respMapperAnnotationClass);
            imports.setHasPrimitiveMapper(true);
            return getBuilderLineForField(RESP_MAPPER, primitiveMapper);
        } catch (NotSupportedTypeForPrimitiveMapperException e) {
            return getBuilderLineForField(RESP_MAPPER,
                    respMapperAnnotationClassName
                            .replace(respMapperAnnotationClassName.charAt(0),
                                    String.valueOf(respMapperAnnotationClassName.charAt(0)).toLowerCase().charAt(0))
                            .replace(REQUEST, EMPTY_STRING)
                            .replace(XML_MAPPER, EMPTY_STRING)
                            .replace(MAPPER, EMPTY_STRING) + TO_MODEL_FOR_PROXY);
        }
    }

    /**
     * Generates line for "withReq" block for certain method.
     *
     * @param method - current method.
     * @return - CodeBlock containing line for request mapper.
     * - null if annotation @RequestMapper is not present or
     * there are both annotations - @Validatable and @NotNull.
     */
    static CodeBlock formRequestMapperLine(Method method, StaticImports imports) {
        Class reqMapperAnnotationClass = Object.class.equals(method.getAnnotation(RequestMapper.class).forAll())
                ? method.getAnnotation(RequestMapper.class).httpProxy()
                : method.getAnnotation(RequestMapper.class).forAll();
        String reqMapperAnnotationClassName = reqMapperAnnotationClass.getSimpleName();
        try {
            String primitiveMapper = getFromModelMappingFromType(reqMapperAnnotationClass);
            imports.setHasPrimitiveMapper(true);
            return getBuilderLineForField(REQ_MAPPER, primitiveMapper);
        } catch (NotSupportedTypeForPrimitiveMapperException e) {
            return getBuilderLineForField(REQ_MAPPER,
                    reqMapperAnnotationClassName
                            .replace(reqMapperAnnotationClassName.charAt(0),
                                    String.valueOf(reqMapperAnnotationClassName.charAt(0)).toLowerCase().charAt(0))
                            .replace(RESPONSE, EMPTY_STRING)
                            .replace(XML_MAPPER, EMPTY_STRING)
                            .replace(MAPPER, EMPTY_STRING) + FROM_MODEL_FOR_PROXY);
        }
    }

    /**
     * Method fill array of codeBlocks for certain method
     * in block based on generated fields.
     *
     * @param data - all data to be filled.
     */
    static void fillCodeBlockHttpProxyBlockData(HttpProxyCodeBlockFillData data) {
        if (data.getBlockData().getGeneratedFields().containsKey(RESPONSE_MAPPER)
                || data.getBlockData().getGeneratedFields().containsKey(REQUEST_MAPPER))
            data.getCodeBlocksBuilder().add(METHOD_PATTERN,
                    of(HTTP_CLIENT_PROXY_BY_URL_WITH_PARAMS,
                            data.getBlockData().getReqMapper().getSimpleName(),
                            data.getBlockData().getRespMapper().getSimpleName(),
                            data.getBlockData().getMethod().getAnnotation(MethodPath.class).methodPath()).toString()).add(NEW_LINE);
        else data.getCodeBlocksBuilder().add(METHOD_PATTERN,
                of(HTTP_CLIENT_PROXY_BY_URL_WITHOUT_PARAMS,
                        data.getBlockData().getMethod().getAnnotation(MethodPath.class).methodPath()).toString()).add(NEW_LINE);

        if (data.getBlockData().getGeneratedFields().containsKey(REQUEST_INTERCEPTOR))
            data.getCodeBlocksBuilder().add(data.getBlockData().getGeneratedFields().get(REQUEST_INTERCEPTOR)).add(NEW_LINE);

        if (data.getBlockData().getGeneratedFields().containsKey(RESPONSE_INTERCEPTOR))
            data.getCodeBlocksBuilder().add(data.getBlockData().getGeneratedFields().get(RESPONSE_INTERCEPTOR)).add(NEW_LINE);

        if (data.getBlockData().getGeneratedFields().containsKey(RESPONSE_MAPPER))
            data.getCodeBlocksBuilder().add(data.getBlockData().getGeneratedFields().get(RESPONSE_MAPPER)).add(NEW_LINE);

        if (data.getBlockData().getGeneratedFields().containsKey(HTTP_PROXY_CONSUMES))
            data.getCodeBlocksBuilder().add(data.getBlockData().getGeneratedFields().get(HTTP_PROXY_CONSUMES)).add(NEW_LINE);
        data.getCodeBlocksBuilder().add(getPresentHttpMethodsAnnotation(data.getBlockData().getGeneratedFields()));

        if (!annotationWithNoRequestWasGenerated(data.getBlockData().getGeneratedFields())) {
            if (data.getBlockData().getGeneratedFields().containsKey(REQUEST_MAPPER))
                data.getCodeBlocksBuilder().add(NEW_LINE).add(data.getBlockData().getGeneratedFields().get(REQUEST_MAPPER));
            if (data.getBlockData().getGeneratedFields().containsKey(HTTP_PROXY_PRODUCES))
                data.getCodeBlocksBuilder().add(NEW_LINE).add(data.getBlockData().getGeneratedFields().get(HTTP_PROXY_PRODUCES));
        } else {
            StringBuilder fieldsForError = new StringBuilder();
            if (data.getBlockData().getGeneratedFields().containsKey(REQUEST_MAPPER))
                fieldsForError.append(format(ANNOTATION, REQUEST_MAPPER.getClassName()));
            if (data.getBlockData().getGeneratedFields().containsKey(HTTP_PROXY_PRODUCES)) {
                if (!isEmpty(fieldsForError.toString())) fieldsForError.append(COMMA);
                if (!data.getBlockData().getGeneratedFields().containsKey(HTTP_PROXY_CONSUMES))
                    data.getBlockData().getImports().setHasMimeTypes(false);
                fieldsForError.append(format(ANNOTATION, HTTP_PROXY_PRODUCES.getClassName()));
            }

            printError(format(METHOD_NAME_STRING, data.getBlockData().getMethod().getName())
                    + format(UNABLE_TO_GENERATE_BECAUSE_OF_ANNOTATION,
                    fieldsForError.toString(),
                    data.getBlockData().getGeneratedFields().containsKey(HTTP_PROXY_TRACE)
                            ? HTTP_PROXY_TRACE.getClassName()
                            : data.getBlockData().getGeneratedFields().containsKey(HTTP_PROXY_HEAD)
                            ? HTTP_PROXY_HEAD.getClassName() : HTTP_PROXY_CONNECT.getClassName()));
            data.getBlockData().getGeneratedFields().remove(HTTP_PROXY_PRODUCES);
            data.getBlockData().getGeneratedFields().remove(REQUEST_MAPPER);
        }

        if (!data.getBlockData().getGeneratedFields().containsKey(REQUEST_MAPPER))
            data.getCodeBlocksBuilder().add(NEW_LINE).add(getBuilderLineForField(PREPARE_METHOD));
    }
}

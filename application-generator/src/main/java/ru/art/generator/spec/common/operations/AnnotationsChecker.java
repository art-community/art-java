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

import ru.art.generator.spec.common.annotation.*;
import ru.art.generator.spec.common.constants.*;
import ru.art.generator.spec.common.exception.*;
import ru.art.generator.spec.http.common.annotation.*;
import ru.art.generator.spec.http.proxyspec.annotation.*;
import ru.art.generator.spec.http.proxyspec.constants.*;
import ru.art.generator.spec.http.proxyspec.exception.*;
import ru.art.generator.spec.http.servicespec.annotation.*;
import ru.art.generator.spec.http.servicespec.constants.*;
import ru.art.generator.spec.http.servicespec.exception.*;
import static java.text.MessageFormat.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.*;
import static ru.art.generator.spec.common.constants.SpecExceptionConstants.DefinitionExceptions.*;
import static ru.art.generator.spec.http.proxyspec.constants.HttpProxySpecAnnotations.*;
import static ru.art.generator.spec.http.servicespec.constants.HttpServiceSpecAnnotations.*;
import java.lang.annotation.*;
import java.lang.reflect.*;

/**
 * Interface containing methods to check annotations for existence.
 */
public interface AnnotationsChecker {

    /**
     * Checks if method has annotation without default value.
     *
     * @param method     - method to check
     * @param annotation - annotation to find
     * @param specType   - generating type of specification.
     * @return true if annotation is defined for method
     * false if annotation isn't defined for method
     */
    static boolean methodHasAnnotation(Method method, Class<? extends Annotation> annotation, SpecificationType specType)
            throws SpecificationTypeDefinitionException, SpecAnnotationDefinitionException {
        switch (specType) {
            case httpServiceSpec:
                try {
                    switch (HttpServiceSpecAnnotations.parseAnnotationClassName(annotation.getSimpleName())) {
                        case LISTEN:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(Listen.class).httpServiceMethodPath());
                        case HTTP_POST:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(HttpPost.class).httpService());
                        case HTTP_GET:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(HttpGet.class).httpService());
                        case HTTP_CONSUMES:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(Consumes.class).httpService());
                        case HTTP_PRODUCES:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(Produces.class).httpService());
                        case REQUEST_MAPPER:
                            return method.isAnnotationPresent(annotation)
                                    && ((!isEmpty(method.getAnnotation(RequestMapper.class).httpService()) &&
                                    !method.getAnnotation(RequestMapper.class).httpService().equals(Object.class))
                                    || (!isEmpty(method.getAnnotation(RequestMapper.class).forAll()) &&
                                    !method.getAnnotation(RequestMapper.class).forAll().equals(Object.class)));
                        case RESPONSE_MAPPER:
                            return method.isAnnotationPresent(annotation)
                                    && ((!isEmpty(method.getAnnotation(ResponseMapper.class).httpService()) &&
                                    !method.getAnnotation(ResponseMapper.class).httpService().equals(Object.class))
                                    || (!isEmpty(method.getAnnotation(ResponseMapper.class).forAll()) &&
                                    !method.getAnnotation(ResponseMapper.class).forAll().equals(Object.class)));
                        case NOT_NULL:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(NotNull.class).httpSpec();
                        case VALIDATABLE:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(Validatable.class).httpSpec();
                        case HTTP_DELETE:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(HttpDelete.class).httpService());
                        case HTTP_HEAD:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(HttpHead.class).httpService());
                        case HTTP_PATCH:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(HttpPatch.class).httpService());
                        case HTTP_OPTIONS:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(HttpOptions.class).httpService());
                        case HTTP_PUT:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(HttpPut.class).httpService());
                        case HTTP_TRACE:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(HttpTrace.class).httpService());
                        case HTTP_CONNECT:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(HttpConnect.class).httpService());
                        case FROM_BODY:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(FromBody.class).httpService();
                        case FROM_QUERY_PARAMS:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(FromQueryParams.class).httpService();
                        case FROM_PATH_PARAMS:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(FromPathParams.class).httpService();
                        case REQUEST_INTERCEPTOR:
                            return method.isAnnotationPresent(annotation)
                                    && ((!isEmpty(method.getAnnotation(RequestInterceptor.class).httpService()) &&
                                    !method.getAnnotation(RequestInterceptor.class).httpService().equals(Object.class))
                                    || (!isEmpty(method.getAnnotation(RequestInterceptor.class).forAll()) &&
                                    !method.getAnnotation(RequestInterceptor.class).forAll().equals(Object.class)));
                        case RESPONSE_INTERCEPTOR:
                            return method.isAnnotationPresent(annotation)
                                    && ((!isEmpty(method.getAnnotation(ResponseInterceptor.class).httpService()) &&
                                    !method.getAnnotation(ResponseInterceptor.class).httpService().equals(Object.class))
                                    || (!isEmpty(method.getAnnotation(ResponseInterceptor.class).forAll()) &&
                                    !method.getAnnotation(ResponseInterceptor.class).forAll().equals(Object.class)));
                        default:
                            return method.isAnnotationPresent(annotation);
                    }
                } catch (HttpServiceSpecAnnotationIdentificationException exception) {
                    throw new SpecAnnotationDefinitionException(format(METHOD_NAME_STRING, method.getName()) + exception.getMessage());
                }
            case httpProxySpec:
                try {
                    switch (HttpProxySpecAnnotations.parseAnnotationClassName(annotation.getSimpleName())) {
                        case HTTP_PROXY_CONSUMES:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(Consumes.class).httpProxy());
                        case HTTP_PROXY_PRODUCES:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(Produces.class).httpProxy());
                        case METHOD_PATH:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(MethodPath.class).methodPath());
                        case REQUEST_MAPPER:
                            return method.isAnnotationPresent(annotation)
                                    && ((!isEmpty(method.getAnnotation(RequestMapper.class).httpProxy()) &&
                                    !method.getAnnotation(RequestMapper.class).httpProxy().equals(Object.class))
                                    || (!isEmpty(method.getAnnotation(RequestMapper.class).forAll()) &&
                                    !method.getAnnotation(RequestMapper.class).forAll().equals(Object.class)));
                        case REQUEST_INTERCEPTOR:
                            return method.isAnnotationPresent(annotation)
                                    && ((!isEmpty(method.getAnnotation(RequestInterceptor.class).httpProxy()) &&
                                    !method.getAnnotation(RequestInterceptor.class).httpProxy().equals(Object.class))
                                    || (!isEmpty(method.getAnnotation(RequestInterceptor.class).forAll()) &&
                                    !method.getAnnotation(RequestInterceptor.class).forAll().equals(Object.class)));
                        case RESPONSE_MAPPER:
                            return method.isAnnotationPresent(annotation)
                                    && ((!isEmpty(method.getAnnotation(ResponseMapper.class).httpProxy()) &&
                                    !method.getAnnotation(ResponseMapper.class).httpProxy().equals(Object.class))
                                    || (!isEmpty(method.getAnnotation(ResponseMapper.class).forAll()) &&
                                    !method.getAnnotation(ResponseMapper.class).forAll().equals(Object.class)));
                        case RESPONSE_INTERCEPTOR:
                            return method.isAnnotationPresent(annotation)
                                    && ((!isEmpty(method.getAnnotation(ResponseInterceptor.class).httpProxy()) &&
                                    !method.getAnnotation(ResponseInterceptor.class).httpProxy().equals(Object.class))
                                    || (!isEmpty(method.getAnnotation(ResponseInterceptor.class).forAll()) &&
                                    !method.getAnnotation(ResponseInterceptor.class).forAll().equals(Object.class)));
                        case FROM_BODY:
                            return method.isAnnotationPresent(annotation)
                                    && !method.getAnnotation(FromBody.class).httpProxy().equals(Object.class);
                        case FROM_PATH_PARAMS:
                            return method.isAnnotationPresent(annotation) && !isEmpty(method.getAnnotation(FromPathParams.class).httpProxy());
                        case FROM_QUERY_PARAMS:
                            return method.isAnnotationPresent(annotation)
                                    && !isEmpty(method.getAnnotation(FromQueryParams.class).httpProxyQueryName())
                                    && !isEmpty(method.getAnnotation(FromQueryParams.class).httpProxyQueryValue());
                        case HTTP_PROXY_POST:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(HttpPost.class).httpProxy();
                        case HTTP_PROXY_GET:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(HttpGet.class).httpProxy();
                        case HTTP_PROXY_DELETE:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(HttpDelete.class).httpProxy();
                        case HTTP_PROXY_HEAD:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(HttpHead.class).httpProxy();
                        case HTTP_PROXY_PATCH:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(HttpPatch.class).httpProxy();
                        case HTTP_PROXY_OPTIONS:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(HttpOptions.class).httpProxy();
                        case HTTP_PROXY_PUT:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(HttpPut.class).httpProxy();
                        case HTTP_PROXY_TRACE:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(HttpTrace.class).httpProxy();
                        case HTTP_PROXY_CONNECT:
                            return method.isAnnotationPresent(annotation) && method.getAnnotation(HttpConnect.class).httpProxy();
                        default:
                            return method.isAnnotationPresent(annotation);
                    }
                } catch (HttpProxySpecAnnotationDefinitionException exception) {
                    throw new SpecAnnotationDefinitionException(format(METHOD_NAME_STRING, method.getName()) + exception.getMessage());
                }
            case soapServiceSpec:
                //TODO
                break;
            case soapProxySpec:
                //TODO
                break;
            case grpcServiceSpec:
                //TODO
                break;
            case grpcProxySpec:
                //TODO
                break;
            default:
                return false;
        }
        throw new SpecificationTypeDefinitionException(format(METHOD_NAME_STRING, method.getName()) +
                format(UNABLE_TO_DEFINE_SPECIFICATION_TYPE, specType.name()));
    }

    /**
     * Checks if class has annotation without default value.
     *
     * @param checkedClass - class to check
     * @param annotation   - annotation to find
     * @param specType     - generating type of specification.
     * @return true if annotation is defined for class
     * false if annotation isn't defined for class
     */
    static boolean classHasAnnotation(Class<?> checkedClass, Class<? extends Annotation> annotation, SpecificationType specType)
            throws SpecificationTypeDefinitionException, SpecAnnotationDefinitionException {
        switch (specType) {
            case httpServiceSpec:
                try {
                    if (HttpServiceSpecAnnotations.parseAnnotationClassName(annotation.getSimpleName()).equals(HTTP_SERVICE))
                        return checkedClass.isAnnotationPresent(annotation) && !isEmpty(checkedClass.getAnnotation(HttpService.class).serve());
                } catch (HttpServiceSpecAnnotationIdentificationException exception) {
                    throw new SpecAnnotationDefinitionException(exception.getMessage());
                }
                throw new SpecAnnotationDefinitionException(format(UNABLE_TO_DEFINE_ANNOTATION, annotation.getSimpleName()));
            case httpProxySpec:
                try {
                    if (HttpProxySpecAnnotations.parseAnnotationClassName(annotation.getSimpleName()).equals(HTTP_PROXY_SERVICE))
                        return checkedClass.isAnnotationPresent(annotation);
                } catch (HttpProxySpecAnnotationDefinitionException exception) {
                    throw new SpecAnnotationDefinitionException(exception.getMessage());
                }
                throw new SpecAnnotationDefinitionException(format(UNABLE_TO_DEFINE_ANNOTATION, annotation.getSimpleName()));
            case soapServiceSpec:
                //TODO
                break;
            case soapProxySpec:
                //TODO
                break;
            case grpcServiceSpec:
                //TODO
                break;
            case grpcProxySpec:
                //TODO
                break;
            default:
                throw new SpecificationTypeDefinitionException(format(UNABLE_TO_DEFINE_SPECIFICATION_TYPE, specType.name()));
        }
        throw new SpecificationTypeDefinitionException(format(UNABLE_TO_DEFINE_SPECIFICATION_TYPE, specType.name()));
    }
}

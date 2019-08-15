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

package ru.art.generator.spec.http.servicespec.constants;

import ru.art.generator.spec.http.servicespec.exception.HttpServiceSpecAnnotationIdentificationException;

import static java.text.MessageFormat.format;
import static ru.art.generator.spec.common.constants.SpecExceptionConstants.DefinitionExceptions.UNABLE_TO_DEFINE_ANNOTATION;

/**
 * Annotations for HttpServiceSpecification.
 * Representing possible methods to execute for HttpService.httpService()
 */
public enum HttpServiceSpecAnnotations {
    NOT_NULL("NotNull"),
    REQUEST_MAPPER("RequestMapper"),
    RESPONSE_MAPPER("ResponseMapper"),
    VALIDATABLE("Validatable"),
    FROM_BODY("FromBody"),
    FROM_MULTIPART("FromMultipart"),
    FROM_PATH_PARAMS("FromPathParams"),
    FROM_QUERY_PARAMS("FromQueryParams"),
    HTTP_CONSUMES("Consumes"),
    HTTP_PRODUCES("Produces"),
    HTTP_GET("HttpGet"),
    HTTP_POST("HttpPost"),
    LISTEN("Listen"),
    HTTP_SERVICE("HttpService"),
    HTTP_DELETE("HttpDelete"),
    HTTP_HEAD("HttpHead"),
    HTTP_OPTIONS("HttpOptions"),
    HTTP_PATCH("HttpPatch"),
    HTTP_PUT("HttpPut"),
    HTTP_TRACE("HttpTrace"),
    HTTP_CONNECT("HttpConnect"),
    REQUEST_INTERCEPTOR("RequestInterceptor"),
    RESPONSE_INTERCEPTOR("ResponseInterceptor");

    private String className;

    public String getClassName() {
        return className;
    }

    HttpServiceSpecAnnotations(String className) {
        this.className = className;
    }

    public static HttpServiceSpecAnnotations parseAnnotationClassName(String annotationClassName) throws HttpServiceSpecAnnotationIdentificationException {
        if (NOT_NULL.className.equals(annotationClassName)) return NOT_NULL;
        if (REQUEST_MAPPER.className.equals(annotationClassName)) return REQUEST_MAPPER;
        if (RESPONSE_MAPPER.className.equals(annotationClassName)) return RESPONSE_MAPPER;
        if (VALIDATABLE.className.equals(annotationClassName)) return VALIDATABLE;
        if (FROM_BODY.className.equals(annotationClassName)) return FROM_BODY;
        if (FROM_MULTIPART.className.equals(annotationClassName)) return FROM_MULTIPART;
        if (FROM_PATH_PARAMS.className.equals(annotationClassName)) return FROM_PATH_PARAMS;
        if (FROM_QUERY_PARAMS.className.equals(annotationClassName)) return FROM_QUERY_PARAMS;
        if (HTTP_CONSUMES.className.equals(annotationClassName)) return HTTP_CONSUMES;
        if (HTTP_PRODUCES.className.equals(annotationClassName)) return HTTP_PRODUCES;
        if (HTTP_GET.className.equals(annotationClassName)) return HTTP_GET;
        if (HTTP_POST.className.equals(annotationClassName)) return HTTP_POST;
        if (LISTEN.className.equals(annotationClassName)) return LISTEN;
        if (HTTP_SERVICE.className.equals(annotationClassName)) return HTTP_SERVICE;
        if (HTTP_DELETE.className.equals(annotationClassName)) return HTTP_DELETE;
        if (HTTP_HEAD.className.equals(annotationClassName)) return HTTP_HEAD;
        if (HTTP_OPTIONS.className.equals(annotationClassName)) return HTTP_OPTIONS;
        if (HTTP_PATCH.className.equals(annotationClassName)) return HTTP_PATCH;
        if (HTTP_PUT.className.equals(annotationClassName)) return HTTP_PUT;
        if (HTTP_TRACE.className.equals(annotationClassName)) return HTTP_TRACE;
        if (HTTP_CONNECT.className.equals(annotationClassName)) return HTTP_CONNECT;
        if (REQUEST_INTERCEPTOR.className.equals(annotationClassName)) return REQUEST_INTERCEPTOR;
        if (RESPONSE_INTERCEPTOR.className.equals(annotationClassName)) return RESPONSE_INTERCEPTOR;

        throw new HttpServiceSpecAnnotationIdentificationException(format(UNABLE_TO_DEFINE_ANNOTATION, annotationClassName));
    }
}

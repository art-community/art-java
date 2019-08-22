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

package ru.art.http.server.service;

import org.jtwig.*;
import java.io.*;
import java.net.*;
import java.util.*;

import static java.util.Objects.*;
import static org.jtwig.JtwigTemplate.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.ArrayConstants.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.extension.InputOutputStreamExtensions.*;
import static ru.art.http.server.constants.HttpServerExceptionMessages.*;
import static ru.art.http.server.module.HttpServerModule.*;
import static ru.art.logging.LoggingModule.*;

public interface HttpResourceService {
    static String getStringResource(final String resource) {
        if (httpServerModule().getResourceConfiguration().getAvailableResourceExtensions().stream().noneMatch(resource::endsWith)) {
            return EMPTY_STRING;
        }
        URL resourceUrl = mapResourceUrl(resource);
        if (isNull(resourceUrl)) {
            return EMPTY_STRING;
        }
        try (InputStream pageStream = resourceUrl.openStream()) {
            String resourceContent = resolveResourceContent(pageStream);
            for (String extension : httpServerModule().getResourceConfiguration().getTemplatingResourceExtensions()) {
                if (resource.endsWith(extension)) {
                    JtwigModel model = new JtwigModel();
                    httpServerModule().getResourceConfiguration()
                            .getTemplateResourceVariables()
                            .forEach(model::with);
                    return inlineTemplate(resourceContent).render(model);
                }
            }
            return resourceContent;
        } catch (IOException e) {
            loggingModule()
                    .getLogger(HttpResourceService.class)
                    .error(RESOURCE_ERROR, e);
        }
        return EMPTY_STRING;
    }

    static String getStringResource(String resource, Map<String, String> templateMapping) {
        if (httpServerModule().getResourceConfiguration().getAvailableResourceExtensions().stream().noneMatch(resource::endsWith)) {
            return EMPTY_STRING;
        }
        URL resourceUrl = mapResourceUrl(resource);
        if (isNull(resourceUrl)) {
            return EMPTY_STRING;
        }
        try (InputStream pageStream = resourceUrl.openStream()) {
            String resourceContent = resolveResourceContent(pageStream);
            for (String extension : httpServerModule().getResourceConfiguration().getTemplatingResourceExtensions()) {
                if (resource.endsWith(extension)) {
                    JtwigModel model = new JtwigModel();
                    templateMapping.forEach(model::with);
                    return inlineTemplate(resourceContent).render(model);
                }
            }
            return resourceContent;
        } catch (IOException e) {
            loggingModule()
                    .getLogger(HttpResourceService.class)
                    .error(RESOURCE_ERROR, e);
        }
        return EMPTY_STRING;
    }

    static String getStringResource(String resource, Map<String, String> resourcePathMapping, Map<String, String> templateMapping) {
        if (httpServerModule().getResourceConfiguration().getAvailableResourceExtensions().stream().noneMatch(resource::endsWith)) {
            return EMPTY_STRING;
        }
        URL resourceUrl = mapResourceUrl(resource, resourcePathMapping);
        if (isNull(resourceUrl)) {
            return EMPTY_STRING;
        }
        try (InputStream pageStream = resourceUrl.openStream()) {
            String resourceContent = resolveResourceContent(pageStream);
            for (String extension : httpServerModule().getResourceConfiguration().getTemplatingResourceExtensions()) {
                if (resource.endsWith(extension)) {
                    JtwigTemplate jtwigTemplate = inlineTemplate(resourceContent);
                    JtwigModel model = new JtwigModel();
                    templateMapping.forEach(model::with);
                    return jtwigTemplate.render(model);
                }
            }
            return resourceContent;
        } catch (IOException e) {
            loggingModule()
                    .getLogger(HttpResourceService.class)
                    .error(RESOURCE_ERROR, e);
        }
        return EMPTY_STRING;
    }

    static byte[] getBinaryResource(String resource) {
        if (httpServerModule().getResourceConfiguration().getAvailableResourceExtensions().stream().noneMatch(resource::endsWith)) {
            return EMPTY_BYTES;
        }
        URL resourceUrl = mapResourceUrl(resource);
        return getBinaryResourceContent(resourceUrl);
    }

    static byte[] getBinaryResourceContent(URL resourceUrl) {
        try (InputStream pageStream = resourceUrl.openStream()) {
            return resolveResourceBinaryContent(pageStream);
        } catch (IOException e) {
            loggingModule()
                    .getLogger(HttpResourceService.class)
                    .error(RESOURCE_ERROR, e);
        }
        return EMPTY_BYTES;
    }

    static String resolveResourceContent(InputStream pageStream) throws IOException {
        return new String(resolveResourceBinaryContent(pageStream), contextConfiguration().getCharset());
    }

    static byte[] getBinaryResource(String resource, Map<String, String> resourcePathMapping) {
        URL resourceUrl = mapResourceUrl(resource, resourcePathMapping);
        return getBinaryResourceContent(resourceUrl);
    }

    static byte[] resolveResourceBinaryContent(InputStream pageStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(pageStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        transferBytes(bufferedInputStream, byteArrayOutputStream, httpServerModule().getResourceConfiguration().getResourceBufferSize());
        return byteArrayOutputStream.toByteArray();
    }

    static URL mapResourceUrl(String resource) {
        String resourceMapping = httpServerModule().getResourceConfiguration().getResourcePathMappings().get(resource);
        if (isNotEmpty(resourceMapping)) {
            resource = resourceMapping;
        }
        return HttpResourceService.class.getClassLoader().getResource(resource);
    }

    static URL mapResourceUrl(String resource, Map<String, String> resourcePathMapping) {
        if (!isEmpty(resourcePathMapping)) {
            resource = resourcePathMapping.get(resource);
        }
        return HttpResourceService.class.getClassLoader().getResource(resource);
    }
}
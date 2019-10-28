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

import com.mitchellbosecke.pebble.*;
import com.mitchellbosecke.pebble.loader.*;
import lombok.experimental.*;
import ru.art.http.server.HttpServerModuleConfiguration.*;
import static java.util.Objects.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.ArrayConstants.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.extension.InputOutputStreamExtensions.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.http.server.HttpServerModuleConfiguration.HttpResourceConfiguration.*;
import static ru.art.http.server.constants.HttpServerExceptionMessages.*;
import static ru.art.http.server.module.HttpServerModule.*;
import static ru.art.logging.LoggingModule.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;

@UtilityClass
public class HttpResourceService {
    public static String getStringResource(String resource) {
        return getStringResource(resource, contextConfiguration().getCharset(), httpServerModule().getResourceConfiguration());
    }

    public static String getStringResource(String resource, HttpResourceConfiguration resourceConfiguration) {
        return getStringResource(resource, contextConfiguration().getCharset(), resourceConfiguration);
    }

    public static String getStringResource(String resource, Charset charset, HttpResourceConfiguration resourceConfiguration) {
        if (resourceConfiguration.getAvailableResourceExtensions().stream().noneMatch(resource::endsWith)) {
            return EMPTY_STRING;
        }
        URL resourceUrl = mapResourceUrl(resource, resourceConfiguration);
        if (isNull(resourceUrl)) {
            return EMPTY_STRING;
        }
        try (InputStream pageStream = resourceUrl.openStream()) {
            String resourceContent = resolveResourceContent(pageStream, charset, resourceConfiguration);
            for (String extension : resourceConfiguration.getTemplatingResourceExtensions()) {
                if (resource.endsWith(extension)) {
                    Map<String, Object> templateContext = mapOf();
                    resourceConfiguration.getTemplateResourceVariables().forEach(templateContext::put);
                    StringLoader templateLoader = new StringLoader();
                    templateLoader.setCharset(charset.name());
                    StringWriter templateWriter = new StringWriter();
                    new PebbleEngine.Builder()
                            .loader(templateLoader)
                            .build()
                            .getTemplate(resourceContent)
                            .evaluate(templateWriter, templateContext);
                    return templateWriter.toString();
                }
            }
            return resourceContent;
        } catch (IOException ioException) {
            loggingModule()
                    .getLogger(HttpResourceService.class)
                    .error(RESOURCE_ERROR, ioException);
        }
        return EMPTY_STRING;
    }

    public static byte[] getBinaryResource(String resource) {
        return getBinaryResource(resource, httpServerModule().getResourceConfiguration());
    }

    public static byte[] getBinaryResource(String resource, HttpResourceConfiguration resourceConfiguration) {
        if (resourceConfiguration.getAvailableResourceExtensions().stream().noneMatch(resource::endsWith)) {
            return EMPTY_BYTES;
        }
        URL resourceUrl = mapResourceUrl(resource, resourceConfiguration);
        return getBinaryResourceContent(resourceUrl, resourceConfiguration);
    }

    private static byte[] getBinaryResourceContent(URL resourceUrl, HttpResourceConfiguration resourceConfiguration) {
        try (InputStream pageStream = resourceUrl.openStream()) {
            return resolveResourceBinaryContent(pageStream, resourceConfiguration);
        } catch (IOException ioException) {
            loggingModule()
                    .getLogger(HttpResourceService.class)
                    .error(RESOURCE_ERROR, ioException);
        }
        return EMPTY_BYTES;
    }

    private static String resolveResourceContent(InputStream pageStream, Charset charset, HttpResourceConfiguration resourceConfiguration) throws IOException {
        return new String(resolveResourceBinaryContent(pageStream, resourceConfiguration), charset);
    }

    private static byte[] resolveResourceBinaryContent(InputStream pageStream, HttpResourceConfiguration resourceConfiguration) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(pageStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        transferBytes(bufferedInputStream, byteArrayOutputStream, resourceConfiguration.getResourceBufferSize());
        return byteArrayOutputStream.toByteArray();
    }

    private static URL mapResourceUrl(String resource, HttpResourceConfiguration resourceConfiguration) {
        HttpResource resourceMapping = resourceConfiguration.getResourceMappings().get(resource);
        if (isNotEmpty(resourceMapping)) {
            resource = resourceMapping.getPath();
        }
        return HttpResourceService.class.getClassLoader().getResource(resource);
    }
}
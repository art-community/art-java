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

package io.art.http.server.service;

import com.mitchellbosecke.pebble.*;
import com.mitchellbosecke.pebble.loader.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.InputStreamExtensions.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.value.factory.PrimitivesFactory.*;
import static io.art.value.immutable.BinaryValue.*;
import static io.art.http.server.HttpServerModuleConfiguration.*;
import static io.art.http.server.constants.HttpServerExceptionMessages.*;
import static io.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.HttpResourceType.*;
import static io.art.http.server.module.HttpServerModule.*;
import static io.art.logging.LoggingModule.*;
import static java.util.Objects.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;

@UtilityClass
public class HttpResourceService {
    public static Value getHttpResource(String resource) {
        return getHttpResource(resource, context().configuration().getCharset(), httpServerModule().getResourceConfiguration());
    }

    public static Value getHttpResource(String resource, HttpResourceConfiguration resourceConfiguration) {
        return getHttpResource(resource, context().configuration().getCharset(), resourceConfiguration);
    }

    public static Value getHttpResource(String request, Charset charset, HttpResourceConfiguration resourceConfiguration) {
        String resourcePath = ifEmpty(cast(request), resourceConfiguration.getDefaultResource().getPath());
        Map<String, HttpResourceExtensionMapping> extensionMappings = resourceConfiguration.getResourceExtensionMappings();
        Map<String, HttpResource> resourcePathMappings = resourceConfiguration.getResourcePathMappings();

        HttpResource resource;
        if (nonNull(resource = resourcePathMappings.get(resourcePath))) {
            return cast(resource.getType() == STRING
                    ? stringPrimitive(getStringResource(resource.getPath(), orElse(resource.getCharset(), charset), resourceConfiguration))
                    : binary(getBinaryResource(resource.getPath(), resourceConfiguration)));
        }

        HttpResourceExtensionMapping resourceExtensionMapping;
        if (resourcePath.contains(DOT) && nonNull(resourceExtensionMapping = extensionMappings.get(resourcePath
                .substring(resourcePath.lastIndexOf(DOT))
                .toLowerCase()))) {
            HttpResource customResource;
            if (nonNull(customResource = resourceExtensionMapping.getCustomHttpResource())) {
                return cast(customResource.getType() == STRING
                        ? stringPrimitive(getStringResource(customResource.getPath(), orElse(customResource.getCharset(), charset), resourceConfiguration))
                        : binary(getBinaryResource(customResource.getPath(), resourceConfiguration)));
            }
            return cast(resourceExtensionMapping.getResourceType() == STRING
                    ? stringPrimitive(getStringResource(resourcePath, orElse(resourceExtensionMapping.getMimeType().getCharset(), charset), resourceConfiguration))
                    : binary(getBinaryResource(resourcePath, resourceConfiguration)));
        }
        HttpResource defaultResource = resourceConfiguration.getDefaultResource();
        return cast(defaultResource.getType() == STRING
                ? stringPrimitive(getStringResource(defaultResource.getPath(), orElse(defaultResource.getCharset(), charset), resourceConfiguration))
                : binary(getBinaryResource(defaultResource.getPath(), resourceConfiguration)));

    }


    public static String getStringResource(String resource) {
        return getStringResource(resource, context().configuration().getCharset(), httpServerModule().getResourceConfiguration());
    }

    public static String getStringResource(String resource, HttpResourceConfiguration resourceConfiguration) {
        return getStringResource(resource, context().configuration().getCharset(), resourceConfiguration);
    }

    public static String getStringResource(String resource, Charset charset, HttpResourceConfiguration resourceConfiguration) {
        if (resourceConfiguration.getResourceExtensionMappings().keySet().stream().noneMatch(resource::endsWith)) {
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
                            .autoEscaping(false)
                            .cacheActive(false)
                            .build()
                            .getTemplate(resourceContent)
                            .evaluate(templateWriter, templateContext);
                    return templateWriter.toString();
                }
            }
            return resourceContent;
        } catch (Throwable throwable) {
            logger(HttpResourceService.class).error(RESOURCE_ERROR, throwable);
        }
        return EMPTY_STRING;
    }


    public static byte[] getBinaryResource(String resource) {
        return getBinaryResource(resource, httpServerModule().getResourceConfiguration());
    }

    public static byte[] getBinaryResource(String resource, HttpResourceConfiguration resourceConfiguration) {
        if (resourceConfiguration.getResourceExtensionMappings().keySet().stream().noneMatch(resource::endsWith)) {
            return EMPTY_BYTES;
        }
        URL resourceUrl = mapResourceUrl(resource, resourceConfiguration);
        return getBinaryResourceContent(resourceUrl, resourceConfiguration);
    }


    private static byte[] getBinaryResourceContent(URL resourceUrl, HttpResourceConfiguration resourceConfiguration) {
        try (InputStream pageStream = resourceUrl.openStream()) {
            return resolveResourceBinaryContent(pageStream, resourceConfiguration);
        } catch (Throwable throwable) {
            logger(HttpResourceService.class).error(RESOURCE_ERROR, throwable);
        }
        return EMPTY_BYTES;
    }

    private static String resolveResourceContent(InputStream pageStream, Charset charset, HttpResourceConfiguration resourceConfiguration) {
        return new String(resolveResourceBinaryContent(pageStream, resourceConfiguration), charset);
    }

    private static byte[] resolveResourceBinaryContent(InputStream pageStream, HttpResourceConfiguration resourceConfiguration) {
        return toByteArray(pageStream, resourceConfiguration.getResourceBufferSize());
    }

    private static URL mapResourceUrl(String resource, HttpResourceConfiguration resourceConfiguration) {
        HttpResource resourcePathMapping = resourceConfiguration.getResourcePathMappings().get(resource);
        if (isNotEmpty(resourcePathMapping)) {
            resource = resourcePathMapping.getPath();
        }
        return HttpResourceService.class.getClassLoader().getResource(resource);
    }
}

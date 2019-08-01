package ru.art.http.server.service;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import static ru.art.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.extension.InputOutputStreamExtensions.transferBytes;
import static ru.art.http.server.constants.HttpServerExceptionMessages.RESOURCE_ERROR;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.INDEX_HTML;
import static ru.art.http.server.module.HttpServerModule.httpServerModule;
import static ru.art.logging.LoggingModule.loggingModule;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.function.Function;

public interface HttpWebResourceService {
    static String getStringResource(final String resource) {
        final String resourceFile = ifEmpty(resource, INDEX_HTML);
        if (httpServerModule().getWebConfiguration().getAvailableResourceExtensions().stream().noneMatch(resourceFile::endsWith)) {
            return EMPTY_STRING;
        }
        URL resourceUrl = mapResourceUrl(resourceFile);
        if (isNull(resourceUrl)) {
            return EMPTY_STRING;
        }
        try (InputStream pageStream = resourceUrl.openStream()) {
            String resourceContent = resolveResourceContent(pageStream);
            for (String extension : httpServerModule().getWebConfiguration().getTemplatingResourceExtensions()) {
                if (resourceFile.endsWith(extension)) {
                    JtwigModel model = new JtwigModel();
                    httpServerModule().getWebConfiguration().getTemplateResourceVariables().forEach((key, value) -> model.with(key, value.apply(key)));
                    return inlineTemplate(resourceContent).render(model);
                }
            }
            return resourceContent;
        } catch (IOException e) {
            loggingModule()
                    .getLogger(HttpWebResourceService.class)
                    .error(RESOURCE_ERROR, e);
        }
        return EMPTY_STRING;
    }

    static String getStringResource(String resource, Map<String, String> templateMapping) {
        final String resourceFile = ifEmpty(resource, INDEX_HTML);
        if (httpServerModule().getWebConfiguration().getAvailableResourceExtensions().stream().noneMatch(resourceFile::endsWith)) {
            return EMPTY_STRING;
        }
        URL resourceUrl = mapResourceUrl(resourceFile);
        if (isNull(resourceUrl)) {
            return EMPTY_STRING;
        }
        try (InputStream pageStream = resourceUrl.openStream()) {
            String resourceContent = resolveResourceContent(pageStream);
            for (String extension : httpServerModule().getWebConfiguration().getTemplatingResourceExtensions()) {
                if (resourceFile.endsWith(extension)) {
                    JtwigModel model = new JtwigModel();
                    templateMapping.forEach(model::with);
                    return inlineTemplate(resourceContent).render(model);
                }
            }
            return resourceContent;
        } catch (IOException e) {
            loggingModule()
                    .getLogger(HttpWebResourceService.class)
                    .error(RESOURCE_ERROR, e);
        }
        return EMPTY_STRING;
    }

    static String getStringResource(String resource, Map<String, String> resourcePathMapping, Map<String, String> templateMapping) {
        final String resourceFile = ifEmpty(resource, INDEX_HTML);
        if (httpServerModule().getWebConfiguration().getAvailableResourceExtensions().stream().noneMatch(resourceFile::endsWith)) {
            return EMPTY_STRING;
        }
        URL resourceUrl = mapResourceUrl(resourceFile, resourcePathMapping);
        if (isNull(resourceUrl)) {
            return EMPTY_STRING;
        }
        try (InputStream pageStream = resourceUrl.openStream()) {
            String resourceContent = resolveResourceContent(pageStream);
            for (String extension : httpServerModule().getWebConfiguration().getTemplatingResourceExtensions()) {
                if (resourceFile.endsWith(extension)) {
                    JtwigTemplate jtwigTemplate = inlineTemplate(resourceContent);
                    JtwigModel model = new JtwigModel();
                    templateMapping.forEach(model::with);
                    return jtwigTemplate.render(model);
                }
            }
            return resourceContent;
        } catch (IOException e) {
            loggingModule()
                    .getLogger(HttpWebResourceService.class)
                    .error(RESOURCE_ERROR, e);
        }
        return EMPTY_STRING;
    }

    static byte[] getBinaryResource(String resource) {
        if (isEmpty(resource)) return EMPTY_BYTES;
        if (httpServerModule().getWebConfiguration().getAvailableResourceExtensions().stream().noneMatch(resource::endsWith)) {
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
                    .getLogger(HttpWebResourceService.class)
                    .error(RESOURCE_ERROR, e);
        }
        return EMPTY_BYTES;
    }

    static String resolveResourceContent(InputStream pageStream) throws IOException {
        return new String(resolveResourceBinaryContent(pageStream), contextConfiguration().getCharset());
    }

    static byte[] getBinaryResource(String resource, Map<String, String> resourcePathMapping) {
        if (isEmpty(resource)) return EMPTY_BYTES;
        URL resourceUrl = mapResourceUrl(resource, resourcePathMapping);
        return getBinaryResourceContent(resourceUrl);
    }

    static byte[] resolveResourceBinaryContent(InputStream pageStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(pageStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        transferBytes(bufferedInputStream, byteArrayOutputStream, httpServerModule().getWebConfiguration().getResourceBufferSize());
        return byteArrayOutputStream.toByteArray();
    }

    static URL mapResourceUrl(String resource) {
        Function<String, String> resourceMapping = httpServerModule().getWebConfiguration().getResourcePathMapping().get(resource);
        if (nonNull(resourceMapping)) {
            resource = resourceMapping.apply(resource);
        }
        return HttpWebResourceService.class.getClassLoader().getResource(resource);
    }

    static URL mapResourceUrl(String resource, Map<String, String> resourcePathMapping) {
        if (!isEmpty(resourcePathMapping)) {
            resource = resourcePathMapping.get(resource);
        }
        return HttpWebResourceService.class.getClassLoader().getResource(resource);
    }
}
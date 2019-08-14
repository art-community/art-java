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

package ru.art.http.server;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http2.Http2Protocol;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.zalando.logbook.servlet.LogbookFilter;
import ru.art.core.constants.InterceptionStrategy;
import ru.art.http.constants.HttpMethodType;
import ru.art.http.constants.MimeToContentTypeMapper;
import ru.art.http.server.exception.HttpServerException;
import ru.art.http.server.interceptor.HttpServerInterceptor;
import ru.art.http.server.interceptor.HttpServerPathInterceptor;
import ru.art.http.server.model.HttpService;
import ru.art.http.server.path.HttpPath;
import ru.art.http.server.specification.HttpServiceSpecification;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.getProperty;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.logging.LogManager.getLogManager;
import static java.util.stream.Collectors.*;
import static ru.art.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.art.core.constants.InterceptionStrategy.*;
import static ru.art.core.constants.NetworkConstants.BROADCAST_IP_ADDRESS;
import static ru.art.core.constants.NetworkConstants.LOCALHOST;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.extension.EqualsCheckingExtensions.ifNotEquals;
import static ru.art.core.extension.ThreadExtensions.thread;
import static ru.art.core.factory.CollectionsFactory.dequeOf;
import static ru.art.core.factory.CollectionsFactory.setOf;
import static ru.art.http.constants.HttpCommonConstants.HTTP_SCHEME;
import static ru.art.http.constants.HttpInterceptorType.AFTER_REQUEST_HANDLING;
import static ru.art.http.constants.HttpInterceptorType.BEFORE_REQUEST_HANDLING;
import static ru.art.http.server.HttpMetricsBinder.bindHttpMetrics;
import static ru.art.http.server.builder.HttpPathBuilder.buildHttpPath;
import static ru.art.http.server.builder.HttpUrlBuilder.UrlInfo;
import static ru.art.http.server.builder.HttpUrlBuilder.buildUrl;
import static ru.art.http.server.constants.HttpServerExceptionMessages.*;
import static ru.art.http.server.constants.HttpServerLoggingMessages.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.*;
import static ru.art.http.server.model.HttpService.HttpMethod;
import static ru.art.http.server.model.HttpService.HttpMethodGroup;
import static ru.art.http.server.module.HttpServerModule.*;
import static ru.art.logging.LoggingModule.loggingModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Deque;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("Duplicates")
public class HttpServer {
    private final static ThreadLocal<InterceptionStrategy> lastRequestInterceptionResult = new ThreadLocal<>();
    private final static ThreadLocal<InterceptionStrategy> lastResponseInterceptionResult = new ThreadLocal<>();
    private final Deque<HttpServerPathInterceptor> requestInterceptors;
    private final Deque<HttpServerPathInterceptor> responseInterceptors;
    private final Set<String> cancelablePaths = setOf();
    private final long creationTimestamp;
    private final Tomcat tomcat;
    private final Logger logger;

    private HttpServer(Tomcat tomcat) {
        this.tomcat = tomcat;
        tomcat.setBaseDir(getProperty(TEMP_DIR_KEY));
        creationTimestamp = currentTimeMillis();
        logger = loggingModule().getLogger(HttpServer.class);
        requestInterceptors = dequeOf();
        responseInterceptors = dequeOf();
    }

    public static HttpServer httpServer() {
        try {
            HttpServer httpServer = new HttpServer(new Tomcat());
            httpServer.start();
            httpServerModuleState().setServer(httpServer);
            return httpServer;
        } catch (Throwable e) {
            throw new HttpServerException(TOMCAT_INITIALIZATION_FAILED, e);
        }
    }

    public static HttpServer httpServerInSeparatedThread() {
        HttpServer httpServer = httpServer();
        thread(HTTP_SERVER_THREAD, httpServer::await);
        return httpServer;
    }

    private static Map<String, HttpMethodGroup> groupHttpMethods(HttpService httpService) {
        return httpService
                .getHttpMethods()
                .stream()
                .collect(groupingBy(method -> method.getPath().getContextPath()))
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, entry -> HttpMethodGroup.builder()
                        .methods(entry.getValue().stream().collect(toMap(HttpMethod::getMethodType, method -> method)))
                        .build()));
    }

    public void await() {
        tomcat.getServer().await();
    }

    public void restart() {
        long millis = currentTimeMillis();
        try {
            tomcat.stop();
            httpServer();
            logger.info(format(TOMCAT_RESTARTED_MESSAGE, currentTimeMillis() - millis));
        } catch (Throwable e) {
            logger.error(TOMCAT_RESTART_FAILED);
        }
    }

    private Context createContext() {
        Context ctx = tomcat.addContext(EMPTY_STRING, getProperty(TEMP_DIR_KEY));
        ctx.setAllowCasualMultipartParsing(httpServerModule().isAllowCasualMultipartParsing());
        if (cancelablePaths.contains(SLASH)) {
            logger.info(HTTP_SERVICES_CANCELED);
            return ctx;
        }
        registerHttpServices(ctx);
        return ctx;
    }

    private void registerHttpServices(Context ctx) {
        httpServices().forEach(service -> registerService(service.getServiceId(), service, ctx));
    }

    private void registerService(String serviceId, HttpServiceSpecification serviceSpec, Context ctx) {
        HttpService httpService = serviceSpec.getHttpService();
        if (cancelablePaths.contains(httpService.getPath())) {
            logger.info(format(HTTP_SERVICE_CANCELED, serviceId, httpService.getPath()));
            return;
        }

        Set<String> registeredServletPaths = setOf();
        Map<String, HttpMethodGroup> methodsGroup = groupHttpMethods(httpService);
        for (HttpMethod method : httpService.getHttpMethods()) {
            if (registeredServletPaths.contains(method.getPath().getContextPath())) {
                continue;
            }
            MimeToContentTypeMapper consumesContentType = method.getConsumesContentType();
            MimeToContentTypeMapper producesContentType = method.getProducesContentType();
            if (nonNull(consumesContentType) && !httpServerModule().getContentMappers().containsKey(consumesContentType.getMimeType())) {
                throw new HttpServerException(format(CONSUMES_CONTENT_TYPE_NOT_SUPPORTED, consumesContentType));
            }
            if (nonNull(producesContentType) && !httpServerModule().getContentMappers().containsKey(producesContentType.getMimeType())) {
                throw new HttpServerException(format(PRODUCES_CONTENT_TYPE_NOT_SUPPORTED, producesContentType));
            }
            HttpPath path = buildHttpPath(extractHttpServicePath(serviceSpec), method.getPath());
            if (cancelablePaths.contains(path.toString())) {
                logger.info(format(HTTP_SERVICE_METHOD_CANCELED, serviceId, method.getMethodId(), httpService.getPath()));
                continue;
            }
            Wrapper defaultServlet = ctx.createWrapper();
            String servletName = DEFAULT_SERVLET + path.getContextPath();
            StringBuilder servletPath = new StringBuilder().append(path.getContextPath());
            if (!path.getPathParameters().isEmpty()) {
                servletPath.append(SLASH).append(WILDCARD);
            }
            defaultServlet.setName(servletName);
            defaultServlet.setServletClass(DefaultServlet.class.getName());
            Map<HttpMethodType, HttpMethod> httpMethods = methodsGroup.get(method.getPath().getContextPath()).getMethods();
            defaultServlet.setServlet(createHttpServlet(httpMethods, path, serviceId));
            ctx.addChild(defaultServlet);
            ctx.addServletMappingDecoded(servletPath.toString(), servletName);
            logHttpServiceRegistration(serviceSpec, httpMethods, path);
            registeredServletPaths.add(path.getContextPath());
        }

    }

    private void logHttpServiceRegistration(HttpServiceSpecification serviceSpec, Map<HttpMethodType, HttpMethod> methods, HttpPath path) {
        try {
            UrlInfo urlInfo = UrlInfo.builder()
                    .port(httpServerModule().getPort())
                    .serverName(httpServerModule().getHost().equals(BROADCAST_IP_ADDRESS) || httpServerModule().getHost().equals(LOCALHOST) ? contextConfiguration().getIpAddress() : httpServerModule().getHost())
                    .scheme(HTTP_SCHEME)
                    .uri(path.toString())
                    .build();
            logger.info(format(HTTP_SERVICE_REGISTERING_MESSAGE, buildUrl(urlInfo), serviceSpec.getServiceId(), methods.keySet()));
        } catch (Throwable e) {
            throw new HttpServerException(e);
        }

    }

    private HttpServiceServlet createHttpServlet(Map<HttpMethodType, HttpMethod> methods, HttpPath path, String serviceId) {
        return new HttpServiceServlet(methods.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, methodEntry -> HttpServletCommand.builder()
                        .path(path.getContextPath())
                        .consumesContentType(methodEntry.getValue().getConsumesContentType())
                        .producesContentType(methodEntry.getValue().getProducesContentType())
                        .ignoreRequestAcceptType(methodEntry.getValue().isIgnoreRequestAcceptType())
                        .ignoreRequestContentType(methodEntry.getValue().isIgnoreRequestContentType())
                        .serviceId(serviceId)
                        .httpMethod(methodEntry.getValue())
                        .build())));
    }

    private String extractHttpServicePath(HttpServiceSpecification serviceSpec) {
        return serviceSpec.getHttpService().getPath();
    }

    private void start() throws LifecycleException {
        getLogManager().reset();
        configureConnector();
        initializeInterceptors();
        Context context = createContext();
        registerInterceptors(context);
        if (httpServerModule().isEnableMetrics()) {
            bindHttpMetrics(context.getManager());
        }
        tomcat.start();
        logger.info(format(TOMCAT_STARTED_MESSAGE, currentTimeMillis() - creationTimestamp));
    }

    private void configureConnector() {
        Connector connector = new Connector();
        connector.setAttribute(ADDRESS_ATTRIBUTE, httpServerModule().getHost());
        connector.setAttribute(MAX_THREADS_ATTRIBUTE, httpServerModule().getMaxThreadsCount());
        connector.setAttribute(MIN_SPARE_THREADS_ATTRIBUTE, httpServerModule().getMinSpareThreadsCount());
        connector.setAttribute(MAX_IDLE_TIME_ATTRIBUTE, httpServerModule().getMaxIdleTime());
        connector.setAttribute(PRESTART_MIN_SPARE_THREADS_ATTRIBUTE, httpServerModule().isPrestartMinSpareThreads());
        connector.addUpgradeProtocol(new Http2Protocol());
        connector.setPort(httpServerModule().getPort());
        tomcat.getService().addConnector(connector);
        tomcat.setConnector(connector);
    }

    private void initializeInterceptors() {
        requestInterceptors.addAll(httpServerModule()
                .getRequestInterceptors()
                .stream()
                .map(interceptor -> new HttpServerPathInterceptor(interceptor, SLASH))
                .collect(toList()));
        responseInterceptors.addAll(httpServerModule()
                .getResponseInterceptors()
                .stream()
                .map(interceptor -> new HttpServerPathInterceptor(interceptor, SLASH))
                .collect(toList()));
        addServicesInterceptors();
    }

    private void registerInterceptors(Context context) {
        int order = 0;
        while (!requestInterceptors.isEmpty()) {
            HttpServerPathInterceptor interceptor = requestInterceptors.removeFirst();
            registerBeforeInterceptor(context, interceptor, interceptor + UNDERSCORE + BEFORE_REQUEST_HANDLING + UNDERSCORE + order);
            order++;
            if (interceptor.getInterceptor().getStrategy() == PROCESS_HANDLING) break;
            if (interceptor.getInterceptor().getStrategy() == STOP_HANDLING) {
                cancelablePaths.add(interceptor.getPath());
                if (httpServerModule().isEnableTracing()) {
                    addLoggingFilter(context);
                }
                return;
            }
        }
        if (httpServerModule().isEnableTracing()) {
            addLoggingFilter(context);
        }
        order = 0;
        while (!responseInterceptors.isEmpty()) {
            HttpServerPathInterceptor interceptor = responseInterceptors.removeLast();
            registerAfterInterceptor(context, interceptor, interceptor + UNDERSCORE + AFTER_REQUEST_HANDLING + UNDERSCORE + order);
            order++;
            if (interceptor.getInterceptor().getStrategy() == PROCESS_HANDLING) break;
            if (interceptor.getInterceptor().getStrategy() == STOP_HANDLING) return;
        }
    }

    private void addLoggingFilter(Context context) {
        FilterDef def = new FilterDef();
        def.setFilterName(LogbookFilter.class.getSimpleName());
        def.setFilter(new LogbookFilter(httpServerModule().getLogbook()));
        FilterMap map = new FilterMap();
        map.setFilterName(LogbookFilter.class.getSimpleName());
        map.addURLPattern(WILDCARD);
        context.addFilterDef(def);
        context.addFilterMap(map);
    }

    private void addServicesInterceptors() {
        httpServices().stream()
                .map(HttpServiceSpecification::getHttpService)
                .forEach(this::addServiceInterceptors);
    }

    private void addServiceInterceptors(HttpService httpService) {
        requestInterceptors.addAll(httpService
                .getRequestInterceptors()
                .stream()
                .map(interceptor -> new HttpServerPathInterceptor(interceptor, httpService.getPath()))
                .collect(toList()));
        responseInterceptors.addAll(httpService
                .getResponseInterceptors()
                .stream()
                .map(interceptor -> new HttpServerPathInterceptor(interceptor, httpService.getPath()))
                .collect(toList()));
        httpService.getHttpMethods().forEach(method -> addMethodInterceptors(httpService.getPath(), method));
    }

    private void addMethodInterceptors(String servicePath, HttpMethod httpMethod) {
        requestInterceptors.addAll(httpMethod.getRequestInterceptors()
                .stream()
                .map(interceptor -> createHttpInterceptor(servicePath, httpMethod, interceptor))
                .collect(toList()));
        responseInterceptors.addAll(httpMethod.getResponseInterceptors()
                .stream()
                .map(interceptor -> createHttpInterceptor(servicePath, httpMethod, interceptor))
                .collect(toList()));
    }

    private HttpServerPathInterceptor createHttpInterceptor(String servicePath, HttpMethod httpMethod, HttpServerInterceptor interceptor) {
        String path = ifEmpty(buildHttpPath(servicePath, httpMethod.getPath()).getContextPath(), SLASH);
        return new HttpServerPathInterceptor(interceptor, path);
    }

    private void registerBeforeInterceptor(Context context, HttpServerPathInterceptor serverPathInterceptor, String filterName) {
        String urlPattern = ifNotEquals(serverPathInterceptor.getPath(), SLASH, serverPathInterceptor.getPath() + SLASH) + WILDCARD;
        logger.info(format(REGISTERING_HTTP_INTERCEPTOR, filterName, urlPattern));
        FilterDef def = new FilterDef();
        def.setFilterName(filterName);
        def.setFilter((request, response, chain) -> {
            InterceptionStrategy strategy = lastRequestInterceptionResult.get();
            if (isNull(strategy) || strategy == NEXT_INTERCEPTOR) {
                lastRequestInterceptionResult.set(serverPathInterceptor.getInterceptor().intercept((HttpServletRequest) request, (HttpServletResponse) response));
                chain.doFilter(request, response);
                return;
            }
            if (strategy == PROCESS_HANDLING) {
                chain.doFilter(request, response);
            }
        });
        FilterMap map = new FilterMap();
        map.setFilterName(filterName);
        map.addURLPattern(urlPattern);
        context.addFilterDef(def);
        context.addFilterMap(map);
    }

    private void registerAfterInterceptor(Context context, HttpServerPathInterceptor serverInterceptor, String filterName) {
        String urlPattern = ifNotEquals(serverInterceptor.getPath(), SLASH, serverInterceptor.getPath() + SLASH) + WILDCARD;
        logger.info(format(REGISTERING_HTTP_INTERCEPTOR, filterName, urlPattern));
        FilterDef def = new FilterDef();
        def.setFilterName(filterName);
        def.setFilter((request, response, chain) -> {
            InterceptionStrategy strategy = lastResponseInterceptionResult.get();
            if (isNull(strategy) || strategy == NEXT_INTERCEPTOR) {
                chain.doFilter(request, response);
                lastResponseInterceptionResult.set(serverInterceptor.getInterceptor().intercept((HttpServletRequest) request, (HttpServletResponse) response));
                return;
            }
            if (strategy == PROCESS_HANDLING) {
                chain.doFilter(request, response);
            }
        });
        FilterMap map = new FilterMap();
        map.setFilterName(filterName);
        map.addURLPattern(urlPattern);
        context.addFilterDef(def);
        context.addFilterMap(map);
    }
}
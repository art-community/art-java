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

package ru.art.generator.spec.http.proxyspec;

import ru.art.generator.common.annotation.NonGenerated;
import ru.art.generator.spec.http.proxyspec.annotation.HttpProxyService;
import ru.art.generator.spec.http.proxyspec.exception.HttpProxySpecGeneratorException;
import static java.text.MessageFormat.format;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.generator.common.constants.Constants.GENERATION_COMPLETED;
import static ru.art.generator.common.constants.Constants.PathAndPackageConstants.MAIN;
import static ru.art.generator.common.constants.Constants.PathAndPackageConstants.RU;
import static ru.art.generator.common.operations.CommonOperations.printError;
import static ru.art.generator.common.operations.CommonOperations.printMessage;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.PathAndPackageConstants.*;
import static ru.art.generator.spec.common.constants.SpecExceptionConstants.SpecificationGeneratorExceptions.MAIN_ANNOTATION_ABSENT;
import static ru.art.generator.spec.common.constants.SpecExceptionConstants.SpecificationGeneratorExceptions.SERVICE_MARKED_IS_NON_GENERATED;
import static ru.art.generator.spec.common.constants.SpecificationType.httpProxySpec;
import static ru.art.generator.spec.common.operations.AnnotationsChecker.classHasAnnotation;
import static ru.art.generator.spec.http.proxyspec.operations.HttpProxySpecificationClassGenerator.*;

/**
 * Main class for generating http proxy specification based on service.
 * Name for generating class equals "ModelClassNameService" + "ProxySpec".
 * Class implements HttpProxyServiceSpecification.
 * Example of service:
 *
 * @HttpProxyService public interface ExampleProxyService {
 * @HttpPost(httpProxy = true)
 * @Produces(httpProxy = "APPLICATION_JSON_UTF8")
 * @ResponseMapper(httpProxy = ExampleMapper.class)
 * @MethodPath(methodPath = "MethodPath")
 * static void testProxy() {
 * doSomething();
 * }
 * }
 * <p>
 * Example of specification:
 * @Getter
 * @RequiredArgsConstructor public class ExampleProxySpec implements HttpProxyServiceSpecification {
 * public static final String METHOD_PATH = "METHOD_PATH";
 * public final String serviceId = "HTTP_PROXY_TEST_PROXY_SERVICE";
 * public final String host;
 * public final Integer port;
 * public final String path;
 * @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
 * @Accessors(fluent = true)
 * private final HttpClientProxyBuilder.HttpClientProxyPreparedBuilder<?, ExampleMapper> MethodPath
 * = HttpClientProxyBuilder.<Object, ExampleMapper> httpClientProxy(getUrl() + "MethodPath")
 * .responseMapper(ExampleMapper.getToModel)
 * .post()
 * .produces(applicationJsonUtf8())
 * .prepare();
 * @Override public <P, R> R executeMethod(String methodId, P request) {
 * switch (methodId) {
 * case METHOD_PATH:
 * return cast(unwrap(MethodPath().execute()));
 * default:
 * throw new UnknownServiceMethodException(serviceId, methodId);
 * }
 * }
 * }
 */
public class Generator {
    /**
     * Perform generation of http proxy specification basing on service interface.
     *
     * @param service        - service interface.
     * @param genPackagePath - path to parent package for service package.
     */
    public static void performGeneration(String genPackagePath, Class service) {
        if (!classHasAnnotation(service, HttpProxyService.class, httpProxySpec)) {
            printError(format(MAIN_ANNOTATION_ABSENT, HttpProxyService.class.getSimpleName(), service.getSimpleName()));
            return;
        }
        if (service.isAnnotationPresent(NonGenerated.class)) {
            printError(format(SERVICE_MARKED_IS_NON_GENERATED, service.getSimpleName()));
            return;
        }

        String genPackageParentPath = genPackagePath.replace(SLASH_SERVICE_SLASH, SLASH_SPEC_SLASH);
        String genParentPackage = genPackageParentPath.substring(genPackageParentPath.indexOf(RU))
                .replace(SLASH, DOT)
                .replace(BACKWARD_SLASH, DOT);
        String genPackage = genParentPackage + DOT + SPEC;
        String jarPathToMain = genPackagePath.substring(0, genPackagePath.lastIndexOf(MAIN) + 5);
        try {
            createProxySpecificationClass(service, genPackage, jarPathToMain);
            methodIds.clear();
            methodAnnotations.clear();
            notGeneratedFieldsForMethod.clear();
            printMessage(GENERATION_COMPLETED);
        } catch (HttpProxySpecGeneratorException exception) {
            methodIds.clear();
            methodAnnotations.clear();
            notGeneratedFieldsForMethod.clear();
            printError(exception.getMessage());
        }

    }
}

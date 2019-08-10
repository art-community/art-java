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

package ru.art.generator.spec.http.servicespec;

import ru.art.generator.common.annotation.NonGenerated;
import ru.art.generator.spec.http.servicespec.annotation.HttpService;
import ru.art.generator.spec.http.servicespec.exception.HttpServiceSpecGeneratorException;
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
import static ru.art.generator.spec.common.constants.SpecificationType.httpServiceSpec;
import static ru.art.generator.spec.common.operations.AnnotationsChecker.classHasAnnotation;
import static ru.art.generator.spec.http.servicespec.operations.HttpServiceSpecificationClassGenerator.*;

/**
 * Main class for generating http service specification based on service.
 * Name for generating class equals "ModelClassNameService" + "Spec". Class implements HttpServiceSpecification.
 * Example of service:
 *
 * @HttpService(serve = "ServicePath")
 * public interface ExampleService {
 * @HttpMethodPost(post = "EXAMPLE_POST")
 * @HttpProduces(produces = "APPLICATION_JSON_UTF8")
 * @HttpConsumes(consumes = "APPLICATION_JSON_UTF8")
 * @RequestMapper(requestClass = ExampleMapper.class)
 * @ResponseMapper(responseClass = ExampleMapper.class)
 * @Listen(listen = "EXAMPLE_PATH")
 * @FromBody
 * @Validatable static String exampleMethod(ExampleRequest req) {
 * return "example";
 * }
 * }
 * <p>
 * Example of specification:
 * public class ExampleServiceSpec implements HttpServiceSpecification {
 * private final String serviceId = EXAMPLE_SERVICE_ID;
 * private final HttpService httpService = httpService()
 * .post("EXAMPLE_POST")
 * .consumes(APPLICATION_JSON_UTF8)
 * .fromBody()
 * .withReq(ExampleMapper.getToModel(), VALIDATABLE)
 * .produces(APPLICATION_JSON_UTF8)
 * .withResp(ExampleMapper.getFromModel())
 * .listen("EXAMPLE_PATH")
 * <p>
 * .serve("ServicePath");
 * @Override public <P, R> R executeMethod(String methodId, P request) {
 * switch (methodId) {
 * case "EXAMPLE_POST":
 * return cast(exampleMethod((ExampleRequest) request));
 * default:
 * throw new UnknownServiceMethodException(serviceId, methodId);
 * }
 * }
 * }
 */
public class Generator {
    /**
     * Perform generation of http service specification basing on service interface.
     *
     * @param service        - service interface.
     * @param genPackagePath - path to parent package for service package.
     */
    public static void performGeneration(String genPackagePath, Class service) {
        if (!classHasAnnotation(service, HttpService.class, httpServiceSpec)) {
            printError(format(MAIN_ANNOTATION_ABSENT, HttpService.class.getSimpleName(), service.getSimpleName()));
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
            createSpecificationClass(service, genPackage, jarPathToMain);
            methodIds.clear();
            methodAnnotations.clear();
            printMessage(GENERATION_COMPLETED);
        } catch (HttpServiceSpecGeneratorException exception) {
            methodIds.clear();
            methodAnnotations.clear();
            printError(exception.getMessage());
        }
    }
}

package ru.art.generator.spec.http.servicespec.operations;

import com.squareup.javapoet.*;
import lombok.Getter;
import ru.art.generator.common.annotation.GenerationException;
import ru.art.generator.spec.common.exception.ExecuteMethodGenerationException;
import ru.art.generator.spec.common.exception.SpecificationTypeDefinitionException;
import ru.art.generator.spec.http.servicespec.annotation.HttpService;
import ru.art.generator.spec.http.servicespec.constants.HttpServiceSpecAnnotations;
import ru.art.generator.spec.http.servicespec.exception.HttpServiceSpecAnnotationIdentificationException;
import ru.art.generator.spec.http.servicespec.exception.HttpServiceSpecGeneratorException;
import ru.art.generator.spec.http.servicespec.model.*;
import ru.art.http.server.specification.HttpServiceSpecification;
import static com.squareup.javapoet.CodeBlock.join;
import static com.squareup.javapoet.CodeBlock.of;
import static java.text.MessageFormat.format;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static ru.art.core.constants.StringConstants.NEW_LINE;
import static ru.art.core.constants.StringConstants.TABULATION;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.generator.common.constants.Constants.*;
import static ru.art.generator.common.constants.Constants.PathAndPackageConstants.BUILD;
import static ru.art.generator.common.constants.Constants.PathAndPackageConstants.SRC_MAIN_JAVA;
import static ru.art.generator.common.constants.Constants.SymbolsAndFormatting.METHOD_PATTERN;
import static ru.art.generator.common.constants.Constants.SymbolsAndFormatting.STRING_PATTERN;
import static ru.art.generator.common.constants.ExceptionConstants.*;
import static ru.art.generator.common.operations.CommonOperations.*;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.*;
import static ru.art.generator.spec.common.constants.SpecExceptionConstants.SpecificationGeneratorExceptions.UNABLE_TO_GENERATE_SPECIFICATION;
import static ru.art.generator.spec.common.constants.SpecificationType.httpServiceSpec;
import static ru.art.generator.spec.common.operations.ConstantsFieldSpecGenerator.generateMethodIdsConstantsBlock;
import static ru.art.generator.spec.common.operations.ConstantsFieldSpecGenerator.generateServiceId;
import static ru.art.generator.spec.common.operations.ExecuteMethodGenerator.generateExecuteMethod;
import static ru.art.generator.spec.http.common.constants.HttpSpecConstants.Errors.INCOMPATIBLE_ANNOTATIONS;
import static ru.art.generator.spec.http.common.operations.HttpAnnotationsChecker.getIncompatibleHttpMethodsAnnotationsForServiceMethod;
import static ru.art.generator.spec.http.common.operations.HttpAnnotationsChecker.serviceMethodHasSeveralHttpMethodsAnnotations;
import static ru.art.generator.spec.http.servicespec.constants.HttpServiceSpecConstants.ExceptionConstants.MULTIPLE_DEFAULT_LISTEN;
import static ru.art.generator.spec.http.servicespec.constants.HttpServiceSpecConstants.*;
import static ru.art.generator.spec.http.servicespec.constants.HttpServiceSpecConstants.Methods.SERVE_METHOD;
import static ru.art.generator.spec.http.servicespec.operations.HttpServiceAuxiliaryOperations.addStaticImports;
import static ru.art.generator.spec.http.servicespec.operations.HttpServiceAuxiliaryOperations.checkHttpServiceAnnotations;
import static ru.art.generator.spec.http.servicespec.operations.HttpServiceBlockGenerator.generateHttpServiceConstant;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Interface contains operations for HTTP specification generator.
 */
public interface HttpServiceSpecificationClassGenerator {

    List<String> methodIds = new ArrayList<>();
    Map<String, HttpServiceMethodsAnnotations> methodAnnotations = new HashMap<>();

    /**
     * Method creates http service specification class based on service interface.
     *
     * @param service       - service model interface.
     * @param genPackage    - string value of spec's package.
     * @param jarPathToMain - classpath from root to main.
     * @throws HttpServiceSpecGeneratorException is thrown when
     *                                           StringIndexOutOfBoundsException, IOException or NullPointerException
     *                                           occurs while writing to file.
     */
    static void createSpecificationClass(Class<?> service, String genPackage, String jarPathToMain)
            throws HttpServiceSpecGeneratorException {
        printMessage(format(START_GENERATING, service.getSimpleName()) + SPECIFICATION);
        Map<Method, StaticImports> importsForMethods = new HashMap<>();
        TypeSpec type = generateSpecification(service, importsForMethods);

        try {
            JavaFile.Builder javaFileBuilder = JavaFile.builder(genPackage, type)
                    .addStaticImport(ru.art.http.server.model.HttpService.class, HTTP_SERVICE)
                    .indent(TABULATION);

            addStaticImports(javaFileBuilder, service, importsForMethods);

            String classJarPath = defineClassJarPath(service, jarPathToMain);

            javaFileBuilder.build().writeTo(new File(classJarPath.subSequence(0, classJarPath.indexOf(BUILD)).toString() + SRC_MAIN_JAVA));
            printMessage(format(GENERATED_SUCCESSFULLY, service.getSimpleName() + SPEC));
        } catch (StringIndexOutOfBoundsException e) {
            throw new HttpServiceSpecGeneratorException(format(UNABLE_TO_PARSE_JAR_PATH, service.getSimpleName()), e);
        } catch (IOException e) {
            throw new HttpServiceSpecGeneratorException(format(UNABLE_TO_WRITE_TO_FILE, service.getSimpleName() + SPECIFICATION), e);
        } catch (NullPointerException e) {
            throw new HttpServiceSpecGeneratorException(format(UNABLE_TO_FIND_A_PATH_FOR_CLASS, service.getSimpleName()), e);
        } catch (Exception e) {
            throw new HttpServiceSpecGeneratorException(format(UNABLE_TO_CREATE_FILE_UNKNOWN_ERROR, service.getSimpleName(), e.getClass().getSimpleName()), e);
        }
    }

    /**
     * Generate block with "serviceId", "httpService" constants and "executeMethod" method for class.
     *
     * @param serviceClass      - service model interface.
     * @param importsForMethods - map with boolean values
     *                          to define imports witch need to be included based on each method of service.
     * @return TypeSpec containing http service specification.
     * @throws HttpServiceSpecGeneratorException is thrown when
     *                                           ExecuteMethodGenerationException or any other exception is occurred.
     */
    static TypeSpec generateSpecification(Class<?> serviceClass, Map<Method, StaticImports> importsForMethods)
            throws HttpServiceSpecGeneratorException {
        try {
            return TypeSpec.classBuilder(serviceClass.getSimpleName() + SPEC)
                    .addAnnotation(Getter.class)
                    .addModifiers(PUBLIC)
                    .addField(generateServiceId(serviceClass, httpServiceSpec))
                    .addField(generateHttpServiceBlock(serviceClass, importsForMethods))
                    .addFields(generateMethodIdsConstantsBlock(httpServiceSpec))
                    .addMethod(generateExecuteMethod(serviceClass, httpServiceSpec))
                    .addSuperinterface(HttpServiceSpecification.class)
                    .addJavadoc(JAVADOC, serviceClass)
                    .build();
        } catch (ExecuteMethodGenerationException | SpecificationTypeDefinitionException e) {
            throw new HttpServiceSpecGeneratorException(e.getMessage(), e);
        } catch (Exception e) {
            throw new HttpServiceSpecGeneratorException(format(UNABLE_TO_GENERATE_SPECIFICATION, serviceClass.getSimpleName(), e.getClass().getSimpleName()), e);
        }
    }

    /**
     * Generate httpService constant by service's methods's annotations
     * if there is only one http method annotation.
     * Http method annotations: @HttpPost, @HttpGet, @HttpDelete, @HttpHead,
     *
     * @param serviceClass      - service model interface.
     * @param importsForMethods - map with boolean values
     *                          to define imports witch need to be included based on each method of service.
     * @return FieldSpec containing httpService constant.
     * @HttpOptions, @HttpPatch, @HttpPut, @HttpTrace, @HttpConnect.
     */
    static FieldSpec generateHttpServiceBlock(Class<?> serviceClass, Map<Method, StaticImports> importsForMethods) {
        List<CodeBlock> codeBlocks = dynamicArrayOf(of(METHOD_PATTERN, HTTP_SERVICE_METHOD));
        Map<String, HttpServiceSpecAnnotations> notGeneratedFields = new LinkedHashMap<>();
        int defaultListenCount = 0;
        for (Method method : serviceClass.getDeclaredMethods()) {
            StaticImports imports = new StaticImports();
            HttpServiceMethodsAnnotations hasAnnotations = new HttpServiceMethodsAnnotations();
            try {
                checkHttpServiceAnnotations(method, httpServiceSpec, hasAnnotations);
            } catch (HttpServiceSpecAnnotationIdentificationException e) {
                printError(format(METHOD_NAME_STRING, method.getName()) + e.getMessage());
            }
            if (serviceMethodHasSeveralHttpMethodsAnnotations(hasAnnotations)) {
                printError(format(METHOD_NAME_STRING, method.getName()) +
                        format(INCOMPATIBLE_ANNOTATIONS, method.getName(),
                                getIncompatibleHttpMethodsAnnotationsForServiceMethod(hasAnnotations)));
                notGeneratedFields.put(method.getName(), null);
                continue;
            }
            HttpServiceConstantGenerationData data = HttpServiceConstantGenerationData.builder()
                    .fillData(HttpServiceCodeBlockFillData.builder()
                            .importsForMethods(importsForMethods)
                            .defaultListenCount(defaultListenCount)
                            .codeBlocks(codeBlocks)
                            .build())
                    .blockData(HttpServiceBlockData.builder()
                            .imports(imports)
                            .hasAnnotations(hasAnnotations)
                            .method(method)
                            .build())
                    .notGeneratedFields(notGeneratedFields)
                    .serviceClass(serviceClass)
                    .build();
            generateHttpServiceConstant(data);

            defaultListenCount = data.getFillData().getDefaultListenCount();
        }
        codeBlocks.add(getBuilderLineForField(SERVE_METHOD, serviceClass.getAnnotation(HttpService.class).serve()));
        if (defaultListenCount > 0)
            printError(MULTIPLE_DEFAULT_LISTEN);
        return (notGeneratedFields.size() != 0)
                ? FieldSpec.builder(ru.art.http.server.model.HttpService.class, HTTP_SERVICE, PUBLIC, FINAL)
                .initializer(join(codeBlocks, NEW_LINE))
                .addAnnotation(AnnotationSpec.builder(GenerationException.class)
                        .addMember(NOT_GENERATED_FIELDS, STRING_PATTERN, notGeneratedFields.keySet())
                        .build())
                .build()
                : FieldSpec.builder(ru.art.http.server.model.HttpService.class, HTTP_SERVICE, PUBLIC, FINAL)
                .initializer(join(codeBlocks, NEW_LINE))
                .build();
    }
}

package ru.art.generator.spec.common.operations;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import ru.art.generator.spec.common.constants.SpecificationType;
import ru.art.generator.spec.common.exception.ExecuteMethodGenerationException;
import ru.art.generator.spec.common.exception.MethodConsumesWithoutParamsException;
import ru.art.generator.spec.http.proxyspec.model.HttpProxyMethodsAnnotations;
import ru.art.generator.spec.http.proxyspec.operations.HttpProxyAuxiliaryOperations;
import ru.art.generator.spec.http.proxyspec.operations.HttpProxySpecificationClassGenerator;
import ru.art.generator.spec.http.servicespec.model.HttpServiceMethodsAnnotations;
import ru.art.generator.spec.http.servicespec.operations.HttpServiceAuxiliaryOperations;
import ru.art.generator.spec.http.servicespec.operations.HttpServiceSpecificationClassGenerator;
import ru.art.service.Specification;
import ru.art.service.exception.UnknownServiceMethodException;
import static com.squareup.javapoet.MethodSpec.methodBuilder;
import static com.squareup.javapoet.TypeVariableName.get;
import static java.text.MessageFormat.format;
import static javax.lang.model.element.Modifier.PUBLIC;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.generator.common.operations.CommonOperations.printError;
import static ru.art.generator.spec.common.constants.CommonSpecGeneratorConstants.ExecuteMethodConstants.*;
import static ru.art.generator.spec.common.constants.SpecExceptionConstants.DefinitionExceptions.UNABLE_TO_DEFINE_METHOD;
import static ru.art.generator.spec.common.constants.SpecExceptionConstants.DefinitionExceptions.UNABLE_TO_DEFINE_SPECIFICATION_TYPE;
import static ru.art.generator.spec.http.common.operations.HttpAnnotationsChecker.amountOfHttpMethodsAnnotations;
import static ru.art.generator.spec.http.common.operations.HttpAnnotationsChecker.serviceMethodHasSeveralHttpMethodsAnnotations;
import java.lang.reflect.Method;

/**
 * Interface for executeMethod's generation for each type of specification.
 */
public interface ExecuteMethodGenerator {

    /**
     * Method generates "executeMethod" for specification.
     *
     * @param service  - class of service.
     * @param specType - generating type of specification.
     * @return MethodSpec representing signature and logic of "executeMethod" method.
     * @throws ExecuteMethodGenerationException when it'a unable to find "executeMethod" in implemented class
     */
    static MethodSpec generateExecuteMethod(Class<?> service, SpecificationType specType) throws ExecuteMethodGenerationException {
        Method method;
        try {
            method = Specification.class.getMethod(EXECUTE_METHOD, String.class, Object.class);
        } catch (NoSuchMethodException e) {
            throw new ExecuteMethodGenerationException(format(UNABLE_TO_DEFINE_METHOD, EXECUTE_METHOD, Specification.class.getSimpleName()));
        }
        if (isEmpty(method))
            throw new ExecuteMethodGenerationException(format(UNABLE_TO_DEFINE_METHOD, EXECUTE_METHOD, Specification.class.getSimpleName()));
        MethodSpec.Builder methodBuilder = methodBuilder(EXECUTE_METHOD)
                .returns(method.getGenericReturnType())
                .addTypeVariable(get(TYPE_P))
                .addTypeVariable(get(TYPE_R))
                .addAnnotation(Override.class)
                .addParameter(ParameterSpec.builder(String.class, METHOD_ID).build())
                .addParameter(ParameterSpec.builder(method.getParameters()[1].getParameterizedType(), REQ).build())
                .addModifiers(PUBLIC)
                .beginControlFlow(SWITCH_BY_METHOD_ID);
        switch (specType) {
            case httpServiceSpec:
                createHttpServiceExecuteMethod(service, methodBuilder);
                break;
            case httpProxySpec:
                createHttpProxyExecuteMethod(service, methodBuilder);
                break;
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
                printError(format(UNABLE_TO_DEFINE_SPECIFICATION_TYPE, specType));
        }


        methodBuilder.addStatement(DEFAULT_IN_EXEC_METHOD, UnknownServiceMethodException.class);
        methodBuilder.endControlFlow();

        return methodBuilder.build();
    }

    /**
     * Method generates body of executeMethod for http service specification.
     *
     * @param service       - class of service.
     * @param methodBuilder - builder for executeMethod method.
     */
    static void createHttpServiceExecuteMethod(Class<?> service, MethodSpec.Builder methodBuilder) {
        if (HttpServiceSpecificationClassGenerator.methodIds.size() > 0) {
            int methodIdIndex = 0;
            for (Method currentMethod : service.getDeclaredMethods()) {
                HttpServiceMethodsAnnotations hasAnnotations = HttpServiceSpecificationClassGenerator.methodAnnotations.get(currentMethod.getName());

                if (isEmpty(hasAnnotations) || amountOfHttpMethodsAnnotations(hasAnnotations) == 0
                        || serviceMethodHasSeveralHttpMethodsAnnotations(hasAnnotations))
                    continue;
                String methodId = HttpServiceSpecificationClassGenerator.methodIds.get(methodIdIndex);
                try {
                    methodBuilder = HttpServiceAuxiliaryOperations.addMethodStatementForExecute(currentMethod,
                            methodBuilder,
                            hasAnnotations,
                            methodId);
                } catch (MethodConsumesWithoutParamsException e) {
                    printError(e.getMessage());
                }
                methodIdIndex++;
            }
        }
    }

    /**
     * Method generates body of executeMethod for http proxy specification.
     *
     * @param service       - class of service.
     * @param methodBuilder - builder for executeMethod method.
     */
    static void createHttpProxyExecuteMethod(Class<?> service, MethodSpec.Builder methodBuilder) {
        if (HttpProxySpecificationClassGenerator.methodIds.size() > 0) {
            int methodIdIndex = 0;
            for (Method currentMethod : service.getDeclaredMethods()) {
                HttpProxyMethodsAnnotations hasAnnotations = HttpProxySpecificationClassGenerator.methodAnnotations.get(currentMethod.getName());

                if (isEmpty(hasAnnotations) || amountOfHttpMethodsAnnotations(hasAnnotations) == 0
                        || serviceMethodHasSeveralHttpMethodsAnnotations(hasAnnotations))
                    continue;
                String methodId = HttpProxySpecificationClassGenerator.methodIds.get(methodIdIndex);
                try {
                    methodBuilder = HttpProxyAuxiliaryOperations.addMethodStatementForExecute(currentMethod, methodBuilder, hasAnnotations, methodId);
                } catch (MethodConsumesWithoutParamsException e) {
                    printError(e.getMessage());
                }
                methodIdIndex++;
            }
        }
    }
}

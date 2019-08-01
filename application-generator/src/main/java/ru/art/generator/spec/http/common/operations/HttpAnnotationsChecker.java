package ru.art.generator.spec.http.common.operations;

import ru.art.generator.spec.http.common.annotation.*;
import ru.art.generator.spec.http.common.model.HttpMethodsAnnotations;
import static ru.art.core.constants.StringConstants.AT_SIGN;
import static ru.art.core.constants.StringConstants.SPACE;

/**
 * Interface with methods to check annotations's states.
 */
public interface HttpAnnotationsChecker {

    /**
     * Method counts the amount of http method's annotations
     * for certain service's method.
     * Http method annotations: @HttpPost, @HttpGet, @HttpDelete, @HttpHead,
     *
     * @param hasAnnotations - model for annotations to check.
     * @return amount of http method's annotations.
     * @HttpOptions, @HttpPatch, @HttpPut, @HttpTrace, @HttpConnect.
     */
    static int amountOfHttpMethodsAnnotations(HttpMethodsAnnotations hasAnnotations) {
        int amount = 0;
        amount = hasAnnotations.isHasPost() ? amount + 1 : amount;
        amount = hasAnnotations.isHasGet() ? amount + 1 : amount;
        amount = hasAnnotations.isHasDelete() ? amount + 1 : amount;
        amount = hasAnnotations.isHasHead() ? amount + 1 : amount;
        amount = hasAnnotations.isHasPatch() ? amount + 1 : amount;
        amount = hasAnnotations.isHasTrace() ? amount + 1 : amount;
        amount = hasAnnotations.isHasPut() ? amount + 1 : amount;
        amount = hasAnnotations.isHasConnect() ? amount + 1 : amount;
        amount = hasAnnotations.isHasOptions() ? amount + 1 : amount;
        return amount;
    }

    /**
     * Checks if service's method has several http method's annotations.
     * Http method annotations: @HttpPost, @HttpGet, @HttpDelete, @HttpHead,
     *
     * @param hasAnnotations - model for annotations to check.
     * @return - true: if method has several http method's annotations.
     * - false: if method has only one or none of http method's annotations.
     * @HttpOptions, @HttpPatch, @HttpPut, @HttpTrace, @HttpConnect.
     */
    static boolean serviceMethodHasSeveralHttpMethodsAnnotations(HttpMethodsAnnotations hasAnnotations) {
        return amountOfHttpMethodsAnnotations(hasAnnotations) > 1;
    }

    /**
     * Method gets string with all http method's annotations' class names
     * presented in method.
     * Http method annotations: @HttpPost, @HttpGet, @HttpDelete, @HttpHead,
     *
     * @param hasAnnotations - model for annotations to check.
     * @return - string of all http method's annotations for method.
     * - empty string: if here are less than 2 http method's annotations for method.
     * @HttpOptions, @HttpPatch, @HttpPut, @HttpTrace, @HttpConnect.
     */
    static String getIncompatibleHttpMethodsAnnotationsForServiceMethod(HttpMethodsAnnotations hasAnnotations) {
        StringBuilder annotations = new StringBuilder();
        if (serviceMethodHasSeveralHttpMethodsAnnotations(hasAnnotations)) {
            if (hasAnnotations.isHasPost())
                annotations.append(AT_SIGN).append(HttpPost.class.getSimpleName()).append(SPACE);
            if (hasAnnotations.isHasGet())
                annotations.append(AT_SIGN).append(HttpGet.class.getSimpleName()).append(SPACE);
            if (hasAnnotations.isHasDelete())
                annotations.append(AT_SIGN).append(HttpDelete.class.getSimpleName()).append(SPACE);
            if (hasAnnotations.isHasHead())
                annotations.append(AT_SIGN).append(HttpHead.class.getSimpleName()).append(SPACE);
            if (hasAnnotations.isHasPatch())
                annotations.append(AT_SIGN).append(HttpPatch.class.getSimpleName()).append(SPACE);
            if (hasAnnotations.isHasTrace())
                annotations.append(AT_SIGN).append(HttpTrace.class.getSimpleName()).append(SPACE);
            if (hasAnnotations.isHasPut())
                annotations.append(AT_SIGN).append(HttpPut.class.getSimpleName()).append(SPACE);
            if (hasAnnotations.isHasOptions())
                annotations.append(AT_SIGN).append(HttpOptions.class.getSimpleName()).append(SPACE);
            if (hasAnnotations.isHasConnect())
                annotations.append(AT_SIGN).append(HttpConnect.class.getSimpleName());
        }
        return annotations.toString();
    }
}

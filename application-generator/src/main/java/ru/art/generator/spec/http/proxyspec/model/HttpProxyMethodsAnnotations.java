package ru.art.generator.spec.http.proxyspec.model;

import lombok.Getter;
import lombok.Setter;
import ru.art.generator.spec.http.common.model.HttpMethodsAnnotations;

/**
 * Model for annotations which need to be checked for methods
 * during http proxy specification's generation.
 */
@Getter
@Setter
public class HttpProxyMethodsAnnotations extends HttpMethodsAnnotations {
    private boolean hasMethodPath;
}

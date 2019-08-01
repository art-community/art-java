package ru.art.generator.spec.http.servicespec.model;

import lombok.Getter;
import lombok.Setter;
import ru.art.generator.spec.http.common.model.HttpMethodsAnnotations;

/**
 * Model for annotations which need to be checked for methods
 * during http service specification's generation.
 */
@Getter
@Setter
public class HttpServiceMethodsAnnotations extends HttpMethodsAnnotations {
    private boolean hasFromMultipart;
    private boolean hasListen;
    private boolean hasNotNull;
    private boolean hasValidatable;
}

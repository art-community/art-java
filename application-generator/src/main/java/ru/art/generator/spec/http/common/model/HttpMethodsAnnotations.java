package ru.art.generator.spec.http.common.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for annotations which need to be checked for methods
 * during any of http specification's generation.
 */
@Getter
@Setter
public class HttpMethodsAnnotations {
    protected boolean hasGet;
    protected boolean hasPost;
    protected boolean hasPut;
    protected boolean hasPatch;
    protected boolean hasOptions;
    protected boolean hasDelete;
    protected boolean hasTrace;
    protected boolean hasConnect;
    protected boolean hasHead;
    protected boolean hasConsumes;
    protected boolean hasProduces;
    protected boolean hasFromBody;
    protected boolean hasFromPathParams;
    protected boolean hasFromQueryParams;
    protected boolean hasRequestMapper;
    protected boolean hasResponseMapper;
    private boolean hasReqInterceptor;
    private boolean hasRespInterceptor;
}

/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.generator.spec.http.common.model;

import lombok.*;

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

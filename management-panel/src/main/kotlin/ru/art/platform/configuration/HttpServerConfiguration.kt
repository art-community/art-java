package ru.art.platform.configuration

import ru.art.config.extensions.http.*
import ru.art.core.factory.CollectionsFactory.*
import ru.art.http.constants.HttpStatus.*
import ru.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.*
import ru.art.http.server.interceptor.*
import ru.art.http.server.interceptor.CookieInterceptor.Error.*
import ru.art.http.server.interceptor.HttpServerInterceptor.*
import ru.art.http.server.service.HttpResourceService.*
import ru.art.metrics.constants.MetricsModuleConstants.*
import ru.art.platform.constants.CommonConstants.TOKEN
import ru.art.platform.service.UserService.authenticate

class HttpServerConfiguration : HttpServerAgileConfiguration() {
    override fun getRequestInterceptors(): MutableList<HttpServerInterceptor> = linkedListOf<HttpServerInterceptor>().apply {
        add(intercept(CookieInterceptor
                .builder()
                .cookieValidator(TOKEN, ::authenticate)
                .pathFilter { path -> !path.contains(METRICS_PATH) }
                .errorProvider { cookieError(UNAUTHORIZED.code, getStringResource(INDEX_HTML)) }
                .build()))
        addAll(super.getRequestInterceptors())
    }
}
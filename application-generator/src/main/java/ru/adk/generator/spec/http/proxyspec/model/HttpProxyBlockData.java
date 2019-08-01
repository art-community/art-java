package ru.adk.generator.spec.http.proxyspec.model;

import com.squareup.javapoet.CodeBlock;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.adk.generator.spec.http.proxyspec.constants.HttpProxySpecAnnotations;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Data used in generating method's constants' codeblocks and
 * in filling array of codeblocks in proper order for these constants.
 */
@Builder
@Getter
@Setter
public class HttpProxyBlockData {
    Method method;
    HttpProxyMethodsAnnotations hasAnnotations;
    Map<HttpProxySpecAnnotations, CodeBlock> generatedFields;
    Class reqMapper;
    Class respMapper;
    StaticImports imports;
}

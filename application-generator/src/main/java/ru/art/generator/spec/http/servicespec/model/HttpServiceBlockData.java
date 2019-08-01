package ru.art.generator.spec.http.servicespec.model;

import com.squareup.javapoet.CodeBlock;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.art.generator.spec.http.servicespec.constants.HttpServiceSpecAnnotations;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Data used in generating httpService constant's codeblocks and
 * in filling array of codeblocks in proper order for generated httpService constant.
 */
@Getter
@Setter
@Builder
public class HttpServiceBlockData {
    StaticImports imports;
    Map<HttpServiceSpecAnnotations, CodeBlock> generatedFields;
    Method method;
    HttpServiceMethodsAnnotations hasAnnotations;
    HttpServiceSpecAnnotations fromAnnotation;
    HttpServiceSpecAnnotations validationPolicy;
}

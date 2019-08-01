package ru.art.generator.spec.http.servicespec.model;

import com.squareup.javapoet.CodeBlock;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Data used for filling array of codeblocks in proper order
 * for generated httpService constant.
 */
@Builder
@Getter
@Setter
public class HttpServiceCodeBlockFillData {
    List<CodeBlock> codeBlocks;
    int defaultListenCount;
    Map<Method, StaticImports> importsForMethods;
}

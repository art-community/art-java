package ru.adk.generator.spec.http.proxyspec.model;

import com.squareup.javapoet.CodeBlock;
import lombok.Builder;
import lombok.Getter;

/**
 * Data used for filling array of codeblocks in proper order
 * for generated method's constants.
 */
@Builder
@Getter
public class HttpProxyCodeBlockFillData {
    HttpProxyBlockData blockData;
    CodeBlock.Builder codeBlocksBuilder;
}

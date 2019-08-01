package ru.art.generator.spec.http.servicespec.model;

import lombok.Builder;
import lombok.Getter;
import ru.art.generator.spec.http.servicespec.constants.HttpServiceSpecAnnotations;

import java.util.Map;

/**
 * Data used generating httpService constant.
 */
@Builder
@Getter
public class HttpServiceConstantGenerationData {
    HttpServiceCodeBlockFillData fillData;
    Map<String, HttpServiceSpecAnnotations> notGeneratedFields;
    Class<?> serviceClass;
    HttpServiceBlockData blockData;
}

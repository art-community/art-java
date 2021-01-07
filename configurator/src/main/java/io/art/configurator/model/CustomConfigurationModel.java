package io.art.configurator.model;

import lombok.*;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class CustomConfigurationModel {
    private final String section;
    private final Class<?> configurationClass;
}

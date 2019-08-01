package ru.art.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import java.util.List;

@Builder
@Getter
public class ServiceDeactivationConfig {
    private boolean deactivated;
    @Singular
    private List<String> deactivatedMethods;
}

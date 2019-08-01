package ru.adk.configurator.api.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.adk.entity.Entity;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ProfileConfiguration {
    private final String profileId;
    private final Entity configuration;
}

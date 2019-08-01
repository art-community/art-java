package ru.art.configurator.api.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.art.entity.Entity;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ProfileConfiguration {
    private final String profileId;
    private final Entity configuration;
}

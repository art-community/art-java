package ru.adk.configurator.api.mapping;

import ru.adk.configurator.api.entity.ProfileConfiguration;
import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.adk.entity.mapper.ValueMapper;
import ru.adk.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.entity.mapper.ValueMapper.mapper;

public interface ProfileConfigurationMapping {
    EntityToModelMapper<ProfileConfiguration> toModel = entity -> ProfileConfiguration.builder()
            .profileId(entity.getString("profileId"))
            .configuration(entity.getEntity("configuration"))
            .build();

    EntityFromModelMapper<ProfileConfiguration> fromModel = model -> entityBuilder()
            .stringField("profileId", model.getProfileId())
            .entityField("configuration", model.getConfiguration())
            .build();

    ValueMapper<ProfileConfiguration, Entity> profileConfigurationMapper = mapper(fromModel, toModel);
}

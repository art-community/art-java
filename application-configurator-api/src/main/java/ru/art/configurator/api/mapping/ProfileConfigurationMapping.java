package ru.art.configurator.api.mapping;

import ru.art.configurator.api.entity.ProfileConfiguration;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.art.entity.mapper.ValueMapper;
import ru.art.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.entity.mapper.ValueMapper.mapper;

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

package ru.adk.configurator.api.mapping;

import ru.adk.configurator.api.entity.Configuration;
import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.adk.entity.mapper.ValueMapper;
import ru.adk.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import static ru.adk.entity.mapper.ValueMapper.mapper;

public interface ConfigurationMapping {
    EntityToModelMapper<Configuration> toModel = entity -> Configuration.builder()
            .configuration(entity)
            .build();

    EntityFromModelMapper<Configuration> fromModel = Configuration::getConfiguration;

    ValueMapper<Configuration, Entity> configurationMapper = mapper(fromModel, toModel);
}

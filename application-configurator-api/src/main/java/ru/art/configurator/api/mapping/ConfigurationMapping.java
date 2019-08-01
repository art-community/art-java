package ru.art.configurator.api.mapping;

import ru.art.configurator.api.entity.Configuration;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.art.entity.mapper.ValueMapper;
import ru.art.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import static ru.art.entity.mapper.ValueMapper.mapper;

public interface ConfigurationMapping {
    EntityToModelMapper<Configuration> toModel = entity -> Configuration.builder()
            .configuration(entity)
            .build();

    EntityFromModelMapper<Configuration> fromModel = Configuration::getConfiguration;

    ValueMapper<Configuration, Entity> configurationMapper = mapper(fromModel, toModel);
}

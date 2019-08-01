package ru.art.configurator.api.mapping;

import ru.art.configurator.api.entity.ModuleConfiguration;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.art.entity.mapper.ValueMapper;
import ru.art.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import static ru.art.configurator.api.mapping.ModuleKeyMapping.moduleKeyMapper;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.entity.mapper.ValueMapper.mapper;

public interface ModuleConfigurationMapping {
    EntityToModelMapper<ModuleConfiguration> toModel = entity -> ModuleConfiguration.builder()
            .moduleKey(moduleKeyMapper.map(entity.getEntity("moduleKey")))
            .configuration(entity.getEntity("configuration"))
            .build();

    EntityFromModelMapper<ModuleConfiguration> fromModel = model -> entityBuilder()
            .entityField("moduleKey", model.getConfiguration())
            .entityField("configuration", model.getConfiguration())
            .build();

    ValueMapper<ModuleConfiguration, Entity> moduleConfigurationMapper = mapper(fromModel, toModel);
}

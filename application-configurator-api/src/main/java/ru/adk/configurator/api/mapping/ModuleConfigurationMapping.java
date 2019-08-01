package ru.adk.configurator.api.mapping;

import ru.adk.configurator.api.entity.ModuleConfiguration;
import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.adk.entity.mapper.ValueMapper;
import ru.adk.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import static ru.adk.configurator.api.mapping.ModuleKeyMapping.moduleKeyMapper;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.entity.mapper.ValueMapper.mapper;

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

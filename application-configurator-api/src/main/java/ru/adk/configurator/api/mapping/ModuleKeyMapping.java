package ru.adk.configurator.api.mapping;


import ru.adk.configurator.api.entity.ModuleKey;
import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.adk.entity.mapper.ValueMapper;
import ru.adk.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.entity.mapper.ValueMapper.mapper;

public interface ModuleKeyMapping {
    EntityToModelMapper<ModuleKey> toModel = entity -> ModuleKey.builder()
            .profileId(entity.getString("profileId"))
            .moduleId(entity.getString("moduleId"))
            .build();

    EntityFromModelMapper<ModuleKey> fromModel = entity -> entityBuilder()
            .stringField("profileId", entity.getProfileId())
            .stringField("moduleId", entity.getModuleId())
            .build();

    ValueMapper<ModuleKey, Entity> moduleKeyMapper = mapper(fromModel, toModel);
}

package ru.art.configurator.api.mapping;


import ru.art.configurator.api.entity.ModuleKey;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.art.entity.mapper.ValueMapper;
import ru.art.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.entity.mapper.ValueMapper.mapper;

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

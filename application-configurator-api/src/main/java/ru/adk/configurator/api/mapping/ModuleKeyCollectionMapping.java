package ru.adk.configurator.api.mapping;


import ru.adk.configurator.api.entity.ModuleKey;
import ru.adk.entity.CollectionValue;
import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import static java.util.stream.Collectors.toSet;
import static ru.adk.configurator.api.mapping.ModuleKeyMapping.moduleKeyMapper;
import static ru.adk.entity.CollectionValuesFactory.entityCollection;
import static ru.adk.entity.mapper.ValueMapper.mapper;
import java.util.Set;

public interface ModuleKeyCollectionMapping {
    ValueToModelMapper<Set<ModuleKey>, CollectionValue<Entity>> toModel = collectionValue -> collectionValue.getEntityList().stream().map(moduleKeyMapper.getToModel()::map).collect(toSet());
    ValueFromModelMapper<Set<ModuleKey>, CollectionValue<Entity>> fromModel = keyCollection -> entityCollection(keyCollection.stream().map(moduleKeyMapper.getFromModel()::map).collect(toSet()));
    ValueMapper<Set<ModuleKey>, CollectionValue<Entity>> moduleKeyCollectionMapper = mapper(fromModel, toModel);
}

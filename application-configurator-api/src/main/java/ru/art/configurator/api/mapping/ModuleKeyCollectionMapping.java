package ru.art.configurator.api.mapping;


import ru.art.configurator.api.entity.ModuleKey;
import ru.art.entity.CollectionValue;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import static java.util.stream.Collectors.toSet;
import static ru.art.configurator.api.mapping.ModuleKeyMapping.moduleKeyMapper;
import static ru.art.entity.CollectionValuesFactory.entityCollection;
import static ru.art.entity.mapper.ValueMapper.mapper;
import java.util.Set;

public interface ModuleKeyCollectionMapping {
    ValueToModelMapper<Set<ModuleKey>, CollectionValue<Entity>> toModel = collectionValue -> collectionValue.getEntityList().stream().map(moduleKeyMapper.getToModel()::map).collect(toSet());
    ValueFromModelMapper<Set<ModuleKey>, CollectionValue<Entity>> fromModel = keyCollection -> entityCollection(keyCollection.stream().map(moduleKeyMapper.getFromModel()::map).collect(toSet()));
    ValueMapper<Set<ModuleKey>, CollectionValue<Entity>> moduleKeyCollectionMapper = mapper(fromModel, toModel);
}

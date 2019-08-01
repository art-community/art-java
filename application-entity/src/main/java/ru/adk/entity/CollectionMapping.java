package ru.adk.entity;

import ru.adk.entity.mapper.ValueMapper;
import static ru.adk.entity.mapper.ValueMapper.mapper;
import java.util.Collection;

public interface CollectionMapping {
    ValueMapper<Collection<String>, CollectionValue<String>> stringCollectionMapper = mapper(CollectionValuesFactory::stringCollection, CollectionValue::getElements);
    ValueMapper<Collection<Integer>, CollectionValue<Integer>> intCollectionMapper = mapper(CollectionValuesFactory::intCollection, CollectionValue::getElements);
    ValueMapper<Collection<Double>, CollectionValue<Double>> doubleCollectionMapper = mapper(CollectionValuesFactory::doubleCollection, CollectionValue::getElements);
    ValueMapper<Collection<Float>, CollectionValue<Float>> floatCollectionMapper = mapper(CollectionValuesFactory::floatCollection, CollectionValue::getElements);
    ValueMapper<Collection<Boolean>, CollectionValue<Boolean>> boolCollectionMapper = mapper(CollectionValuesFactory::boolCollection, CollectionValue::getElements);
    ValueMapper<Collection<Long>, CollectionValue<Long>> longCollectionMapper = mapper(CollectionValuesFactory::longCollection, CollectionValue::getElements);
    ValueMapper<Collection<Entity>, CollectionValue<Entity>> entityCollectionMapper = mapper(CollectionValuesFactory::entityCollection, CollectionValue::getElements);
    ValueMapper<Collection<Value>, CollectionValue<Value>> valueCollectionMapper = mapper(CollectionValuesFactory::valueCollection, CollectionValue::getElements);
}

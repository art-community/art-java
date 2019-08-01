package ru.art.module.executor.mapper;

import ru.art.entity.Value;
import ru.art.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.module.executor.mapper.ExecutorResponseMapper.Fields.RESPONSE;

/**
 * Mapper for response which has Value type
 * So it just moves Value to Entity
 * Used in http server
 */
public interface ExecutorResponseMapper {
    EntityFromModelMapper<Value> executionResponseFromModelMapper = response -> entityBuilder()
            .valueField(RESPONSE, response)
            .build();

    interface Fields {
        String RESPONSE = "response";
    }
}

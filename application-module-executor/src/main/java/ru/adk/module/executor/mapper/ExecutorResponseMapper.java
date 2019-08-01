package ru.adk.module.executor.mapper;

import ru.adk.entity.Value;
import ru.adk.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.module.executor.mapper.ExecutorResponseMapper.Fields.RESPONSE;

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

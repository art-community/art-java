package ru.adk.example.api.mapping;

import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.example.api.model.ExampleStateModel;

public interface ExampleStateModelMapper {
    String serviceRequests = "serviceRequests";

    ValueToModelMapper<ExampleStateModel, Entity> toExampleStateModel = entity -> ExampleStateModel.builder()
            .serviceRequests(entity.getInt(serviceRequests))
            .build();

    ValueFromModelMapper<ExampleStateModel, Entity> fromExampleStateModel = model -> Entity.entityBuilder()
            .intField(serviceRequests, model.getServiceRequests())
            .build();
}

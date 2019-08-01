package ru.art.example.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.example.api.model.ExampleStateModel;

public interface ExampleStateModelMapper {
    String serviceRequests = "serviceRequests";

    ValueToModelMapper<ExampleStateModel, Entity> toExampleStateModel = entity -> ExampleStateModel.builder()
            .serviceRequests(entity.getInt(serviceRequests))
            .build();

    ValueFromModelMapper<ExampleStateModel, Entity> fromExampleStateModel = model -> Entity.entityBuilder()
            .intField(serviceRequests, model.getServiceRequests())
            .build();
}

package ru.art.example.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.XmlEntity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.entity.mapper.ValueToModelMapper.XmlEntityToModelMapper;
import ru.art.example.api.model.ExampleModel;

public interface ExampleModelMapper {
    String exampleModel = "exampleModel";
    String exampleStringField = "exampleStringField";
    String exampleIntegerField = "exampleIntegerField";
    String exampleBooleanField = "exampleBooleanField";

    ValueToModelMapper<ExampleModel, Entity> toExampleModel = entity -> ExampleModel.builder()
            .exampleStringField(entity.getString(exampleStringField))
            .exampleIntegerField(entity.getInt(exampleIntegerField))
            .exampleBooleanField(entity.getBool(exampleBooleanField))
            .build();

    ValueFromModelMapper<ExampleModel, Entity> fromExampleModel = model -> Entity.entityBuilder()
            .stringField(exampleStringField, model.getExampleStringField())
            .intField(exampleIntegerField, model.getExampleIntegerField())
            .boolField(exampleBooleanField, model.isExampleBooleanField())
            .build();

    XmlEntityToModelMapper<ExampleModel> toExampleModelXml = xmlEntity -> ExampleModel.builder()
            .exampleBooleanField(Boolean.valueOf(xmlEntity.getValueByTag(exampleBooleanField)))
            .exampleIntegerField(Integer.valueOf(xmlEntity.getValueByTag(exampleIntegerField)))
            .exampleStringField(xmlEntity.getValueByTag(exampleStringField))
            .build();


    XmlEntityFromModelMapper<ExampleModel> fromExampleModelXml = model -> XmlEntity.xmlEntityBuilder()
            .tag(exampleModel)
            .child().tag(exampleStringField).stringValue(model.getExampleStringField()).build()
            .child().tag(exampleIntegerField).intValue(model.getExampleIntegerField()).build()
            .child().tag(exampleBooleanField).booleanValueStringFormat(model.isExampleBooleanField()).build()
            .create();
}

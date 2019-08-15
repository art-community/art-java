/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

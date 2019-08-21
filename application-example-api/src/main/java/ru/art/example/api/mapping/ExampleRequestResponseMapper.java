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

import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.entity.mapper.ValueFromModelMapper.*;
import ru.art.entity.mapper.ValueToModelMapper.*;
import ru.art.example.api.model.*;

import static ru.art.entity.XmlEntity.*;
import static ru.art.example.api.mapping.ExampleModelMapper.*;

public interface ExampleRequestResponseMapper {
    interface ExampleRequestMapper {
        String innerModel = "innerModel";
        String primitiveTypeInt = "primitiveTypeInt";
        String collection = "collection";
        String exampleRequest = "exampleRequest";

        ValueToModelMapper<ExampleRequest, Entity> toExampleRequest = entity -> ExampleRequest.builder()
                .innerModel(entity.getValue(innerModel, ExampleModelMapper.toExampleModel))
                .primitiveTypeInt(entity.getInt(primitiveTypeInt))
                .collection(entity.getStringList(collection))
                .build();

        ValueFromModelMapper<ExampleRequest, Entity> fromExampleRequest = model -> Entity.entityBuilder()
                .entityField(innerModel, model.getInnerModel(), ExampleModelMapper.fromExampleModel)
                .intField(primitiveTypeInt, model.getPrimitiveTypeInt())
                .stringCollectionField(collection, model.getCollection())
                .build();

        //todo collection mapping
        XmlEntityFromModelMapper<ExampleRequest> fromExampleXmlRequest = model -> xmlEntityBuilder()
                .tag(exampleRequest)
                .child().tag(innerModel).child(fromExampleModelXml.map(model.getInnerModel())).build()
                .child().tag(primitiveTypeInt).intValue(model.getPrimitiveTypeInt()).build()
                .create();

        //todo
        XmlEntityToModelMapper<ExampleRequest> toExampleXmlRequest = xmlEntity -> ExampleRequest.builder()
                .primitiveTypeInt(Integer.valueOf(xmlEntity.getValueByTag(primitiveTypeInt)))
                .build();
    }

    interface ExampleResponseMapper {
        String responseMessage = "responseMessage";
        String exampleResponse = "exampleResponse";

        ValueToModelMapper<ExampleResponse, Entity> toExampleResponse = entity -> ExampleResponse.builder()
                .responseMessage(entity.getString(responseMessage))
                .build();

        ValueFromModelMapper<ExampleResponse, Entity> fromExampleResponse = model -> Entity.entityBuilder()
                .stringField(responseMessage, model.getResponseMessage())
                .build();

        XmlEntityFromModelMapper<ExampleResponse> fromExampleXmlResponse = model -> xmlEntityBuilder()
                .tag(exampleResponse)
                .child().tag(responseMessage).stringValue(model.getResponseMessage()).build()
                .create();

        XmlEntityToModelMapper<ExampleResponse> toExampleXmlResponse = xmlEntity -> ExampleResponse.builder()
                .responseMessage(xmlEntity.getValueByTag(responseMessage))
                .build();
    }
}

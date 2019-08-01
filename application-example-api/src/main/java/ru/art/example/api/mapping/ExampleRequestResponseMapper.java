package ru.art.example.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.entity.mapper.ValueToModelMapper.XmlEntityToModelMapper;
import ru.art.example.api.model.ExampleRequest;
import ru.art.example.api.model.ExampleResponse;
import static ru.art.entity.XmlEntity.xmlEntityBuilder;
import static ru.art.example.api.mapping.ExampleModelMapper.fromExampleModelXml;

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

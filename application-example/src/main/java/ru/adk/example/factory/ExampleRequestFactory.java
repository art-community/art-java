package ru.adk.example.factory;

import ru.adk.example.api.model.ExampleModel;
import ru.adk.example.api.model.ExampleRequest;
import java.util.ArrayList;

/**
 * This is request factory
 * made for fast ExampleRequest generation
 */
public class ExampleRequestFactory {
    public static ExampleRequest getExampleRequest() {
        return ExampleRequest.builder()
                .innerModel(ExampleModel.builder()
                        .exampleBooleanField(true)
                        .exampleIntegerField(10)
                        .exampleStringField("Example String")
                        .build())
                .primitiveTypeInt(15)
                .collection(new ArrayList<>())
                .build();
    }
}

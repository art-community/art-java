/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.example.factory;

import ru.art.example.api.model.ExampleModel;
import ru.art.example.api.model.ExampleRequest;
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

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

package ru.art.example.service;

import ru.art.entity.*;
import ru.art.example.api.model.*;
import static java.time.temporal.ChronoUnit.*;
import static ru.art.entity.constants.ValueType.PrimitiveType.*;
import static ru.art.example.api.constants.ExampleApiConstants.*;
import static ru.art.example.api.constants.ExampleApiConstants.Methods.*;
import static ru.art.example.dao.TableDualDao.*;
import static ru.art.example.factory.ExampleRequestFactory.*;
import static ru.art.example.module.ExampleModule.*;
import static ru.art.json.descriptor.JsonEntityReader.*;
import static ru.art.json.descriptor.JsonEntityWriter.*;
import static ru.art.json.module.JsonModule.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.protobuf.descriptor.ProtobufEntityReader.*;
import static ru.art.protobuf.descriptor.ProtobufEntityWriter.*;
import static ru.art.rocks.db.dao.RocksDbPrimitiveDao.*;
import static ru.art.service.ServiceController.*;
import static ru.art.task.deferred.executor.SchedulerModuleActions.*;
import static ru.art.task.deferred.executor.TaskFactory.*;
import java.time.*;
import java.util.*;

/**
 * ExampleService shows all main features of Application Development Kit
 */

public interface ExampleService {

    /**
     * Method shows simply handling of incoming request and how to return simply response
     *
     * @param request - example request
     * @return ExampleResponse object with response message
     */
    static ExampleResponse requestResponseHandlingExample(ExampleRequest request) {
        loggingModule().getLogger().info("requestResponseHandlingExample method isWorking");

        // getting data from example request
        loggingModule().getLogger().info("Request inner model: " + request.getInnerModel().toString());
        loggingModule().getLogger().info("Request primitive value: " + request.getPrimitiveTypeInt().toString());
        loggingModule().getLogger().info("Request collection: " + request.getCollection());

        //here can be some logic

        // formatting example response
        return ExampleResponse.builder()
                .responseMessage("This is response message")
                .build();
    }

    /**
     * Method shows usage of config values
     * Config values are variables, which can be changed in runtime
     */
    static void usingConfigurationValuesExample() {
        loggingModule().getLogger().info("usingConfigurationValuesExample method isWorking");

        int intConfigValue = exampleModule().getConfigExampleIntValue();
        String stringConfigValue = exampleModule().getConfigExampleStringValue();

        loggingModule().getLogger().info("Integer value from configuration: " + intConfigValue);
        loggingModule().getLogger().info("String value from configuration: " + stringConfigValue);
    }

    /**
     * Method shows how to send soap request
     */
    static void soapClientExample() {
        loggingModule().getLogger().info("soapClientExample method isWorking");
        // todo after proxy specification
    }

    /**
     * Method shows how to call method of other module by http protocol
     * You should use http communication specification for that
     */
    static void httpClientExample() {
        loggingModule().getLogger().info("httpClientExample method isWorking");

        // generating request
        ExampleRequest request = getExampleRequest();

        // executing method by proxy specification
        Optional<ExampleResponse> exampleResponse = executeServiceMethod(EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID, REQUEST_RESPONSE_HANDLING_EXAMPLE, request);

        // handling response
        loggingModule().getLogger()
                .info("httpClientExample method response: " +
                        exampleResponse.orElse(ExampleResponse.builder().
                                responseMessage("Some problems")
                                .build()));
    }

    /**
     * Method shows how to call method of module by proto protocol
     * You should use protobuf proxy specification for that
     * <p>
     * In this example calls method of this module, but
     * you can call methods of different modules
     */
    static void protobufClientExample() {
        loggingModule().getLogger().info("protobufClientExample method isWorking");

        // generating request
        ExampleRequest request = getExampleRequest();

        // executing method by proxy specification
        Optional<ExampleResponse> exampleResponse = executeServiceMethod(EXAMPLE_GRPC_COMMUNICATION_SERVICE_ID, REQUEST_RESPONSE_HANDLING_EXAMPLE, request);

        // handling response
        loggingModule().getLogger()
                .info("protobufClientExample method response: " +
                        exampleResponse.orElse(ExampleResponse.builder().
                                responseMessage("Some problems")
                                .build()));
    }

    /**
     * Method shows how to get some data from database
     * sqlModule uses Jooq framework, so you can also read Jooq documentation
     * http://www.jooq.org/learn/
     * <p>
     * executing request for checking database is available
     * oracle example "select 1 from dual"
     * db queries are moved to dao
     */
    static void sqlExample() {
        loggingModule().getLogger().info("sqlExample method isWorking");

        String testQueryResult = testQuery();
        loggingModule().getLogger().info("Test query result is: " + testQueryResult);
    }

    /**
     * Method shows how to use RocksDB
     * Adding and getting values
     */
    static void rocksDbExample() {
        loggingModule().getLogger().info("rocksDbExample method isWorking");

        // supports primitives and Value
        String key = "key";
        String value = "value";

        // adding value
        put(key, value);

        // getting value
        getString(key).ifPresent(string -> loggingModule().getLogger().info("Value from rocks database" + string));
        delete(key);
    }

    /**
     * Method shows using of logging module
     */
    static void loggingExample() {
        loggingModule().getLogger().info("loggingExample method isWorking");

        // logging some simple info
        loggingModule().getLogger().info("Info message");

        // logging different warnings
        loggingModule().getLogger().warn("Warning message");

        // logging errors
        loggingModule().getLogger().error("Error Message");
    }

    /**
     * Method shows how to convert json to Value and backwards
     */
    static void jsonReadWriteExample() {
        loggingModule().getLogger().info("jsonReadWriteExample method isWorking");

        String jsonString = "{\"FieldName\" : \"FieldValue\"}";

        // mapping string to value
        Value jsonValue = readJson(jsonModule().getObjectMapper().getFactory(), jsonString);

        // mapping value to string
        String jsonMappedString = writeJson(jsonModule().getObjectMapper().getFactory(), jsonValue, false);

        // logging results
        loggingModule().getLogger().info(jsonValue);
        loggingModule().getLogger().info(jsonMappedString);
    }

    /**
     * Method shows how to convert Value to ProtobufValue and backwards
     */
    static void protobufReadWriteExample() {
        loggingModule().getLogger().info("protobufReadWriteExample method isWorking");

        Value exampleValue = new Primitive(10, INT);

        // mapping value to protobufValue
        com.google.protobuf.Value protobufValue = writeProtobuf(exampleValue);

        // mapping protobufValue to value
        Value mappedValue = readProtobuf(protobufValue);
    }


    /**
     * Method shows how to execute something in other other thread in needed time
     */
    static void asyncTaskExecutingExample() {
        loggingModule().getLogger().info("asyncTaskExecutingExample method isWorking");

        asynchronousPeriod(uniqueRunnableTask(() -> loggingModule()
                        .getLogger()
                        .info("Asynchronous task was executed at" + new Date().toString())),
                Duration.of(30, SECONDS));
    }

    /**
     * Method shows how to get module state
     *
     * @return number of services calls
     */
    static ExampleStateModel getExampleModuleState() {
        loggingModule().getLogger().info("getExampleModuleState method isWorking");

        return ExampleStateModel.builder()
                .serviceRequests(exampleState().getRequests())
                .build();
    }
}


import org.junit.BeforeClass;
import org.junit.Test;
import ru.art.example.api.model.ExampleRequest;
import ru.art.example.api.model.ExampleResponse;
import ru.art.example.api.model.ExampleStateModel;
import ru.art.example.api.communication.http.ExampleServiceHttpCommunicationSpecification;
import static ru.art.example.api.constants.ExampleApiConstants.EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID;
import static ru.art.example.api.constants.ExampleApiConstants.EXAMPLE_SERVICE_ID;
import static ru.art.example.api.constants.ExampleApiConstants.Methods.*;
import static ru.art.example.api.constants.ExampleApiConstants.Paths.REQUEST_RESPONSE_HANDLING_EXAMPLE_PATH;
import static ru.art.example.api.mapping.ExampleRequestResponseMapper.ExampleRequestMapper.fromExampleRequest;
import static ru.art.example.api.mapping.ExampleRequestResponseMapper.ExampleResponseMapper.toExampleResponse;
import static ru.art.example.factory.ExampleRequestFactory.getExampleRequest;
import static ru.art.http.client.communicator.HttpCommunicator.httpCommunicator;
import static ru.art.http.client.module.HttpClientModule.httpClientModule;
import static ru.art.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import static ru.art.service.ServiceController.executeServiceMethod;
import static ru.art.service.ServiceModule.serviceModule;
import java.util.Optional;

/**
 * ExampleService methods http caller
 * Made to show how to call service methods
 * and to see how do they work in debug
 * <p>
 * Start ExampleModule before executing
 */
public class ExampleServiceHttpTest {

    @BeforeClass
    public static void beforeClass() {
        ExampleTestInitializer.startExample();
    }

    @BeforeClass
    public static void initContext() {
        serviceModule()
                .getServiceRegistry()
                .registerService(new ExampleServiceHttpCommunicationSpecification());
    }

    @Test
    public void requestResponseHandlingExample() {
        ExampleRequest request = getExampleRequest();
        Optional<ExampleResponse> exampleResponse = executeServiceMethod(EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID, REQUEST_RESPONSE_HANDLING_EXAMPLE, request);
        assert (exampleResponse.isPresent());
        System.out.println(exampleResponse.get());
    }

    @Test
    public void usingConfigurationValuesExample() {
        executeServiceMethod(EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID, USING_CONFIGURATION_VALUES_EXAMPLE);
    }

    @Test
    public void soapClientExample() {
        executeServiceMethod(EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID, SOAP_CLIENT_EXAMPLE);
    }

    @Test
    public void httpClientExample() {
        executeServiceMethod(EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID, HTTP_CLIENT_EXAMPLE);
    }

    @Test
    public void protobufClientExample() {
        executeServiceMethod(EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID, GRPC_CLIENT_EXAMPLE);
    }

    @Test
    public void sqlExample() {
        executeServiceMethod(EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID, SQL_EXAMPLE);
    }

    @Test
    public void rocksDbExample() {
        executeServiceMethod(EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID, ROCKS_DB_EXAMPLE);
    }

    @Test
    public void loggingExample() {
        executeServiceMethod(EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID, LOGGING_EXAMPLE);
    }

    @Test
    public void jsonReadWriteExample() {
        executeServiceMethod(EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID, JSON_READ_WRITE_EXAMPLE);
    }

    @Test
    public void protobufReadWriteExample() {
        executeServiceMethod(EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID, PROTOBUF_READ_WRITE_EXAMPLE);
    }

    @Test
    public void asyncTaskExecutingExample() {
        executeServiceMethod(EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID, ASYNC_TASK_EXECUTING_EXAMPLE);
    }

    @Test
    public void getExampleModuleState() {
        Optional<ExampleStateModel> exampleResponse = executeServiceMethod(EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID, GET_EXAMPLE_MODULE_STATE);
        assert (exampleResponse.isPresent());
        System.out.println(exampleResponse.get());
    }

    /**
     * This method shows how to call method by http without proxy specification
     * So you see that using proxy makes your code easier to read
     */
    @Test
    public void httpDirectRequestExample() {
        Optional<ExampleResponse> exampleResponse = httpCommunicator(httpClientModule().getCommunicationTargetConfiguration(EXAMPLE_SERVICE_ID).toBuilder().build().addPath(REQUEST_RESPONSE_HANDLING_EXAMPLE_PATH))
                .consumes(applicationJsonUtf8())
                .responseMapper(toExampleResponse)
                .post()
                .requestMapper(fromExampleRequest)
                .produces(applicationJsonUtf8())
                .execute(getExampleRequest());

        assert (exampleResponse.isPresent());
        System.out.println(exampleResponse.get());
    }
}

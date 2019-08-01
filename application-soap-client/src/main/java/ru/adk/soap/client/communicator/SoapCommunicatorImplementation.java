package ru.adk.soap.client.communicator;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import ru.adk.core.validator.BuilderValidator;
import ru.adk.entity.XmlEntity;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.http.client.handler.HttpCommunicationCancellationHandler;
import ru.adk.http.client.handler.HttpCommunicationExceptionHandler;
import ru.adk.http.client.handler.HttpCommunicationResponseHandler;
import ru.adk.http.client.interceptor.HttpClientInterceptor;
import ru.adk.http.client.model.HttpCommunicationTargetConfiguration;
import ru.adk.soap.client.communicator.SoapCommunicator.SoapAsynchronousCommunicator;
import ru.adk.soap.content.mapper.SoapMimeToContentTypeMapper;
import static ru.adk.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.adk.core.constants.StringConstants.COLON;
import static ru.adk.core.constants.StringConstants.SCHEME_DELIMITER;
import static ru.adk.core.context.Context.contextConfiguration;
import static ru.adk.core.extension.NullCheckingExtensions.getOrElse;
import static ru.adk.core.extension.StringExtensions.emptyIfNull;
import static ru.adk.http.client.module.HttpClientModule.httpClientModule;
import static ru.adk.soap.client.module.SoapClientModule.soapClientModule;
import java.nio.charset.Charset;
import java.util.Optional;


public class SoapCommunicatorImplementation implements SoapCommunicator, SoapAsynchronousCommunicator {
    private final BuilderValidator validator = new BuilderValidator(SoapCommunicator.class.getName());
    private final SoapCommunicationConfiguration configuration = new SoapCommunicationConfiguration();

    SoapCommunicatorImplementation(String url) {
        this(HttpCommunicationTargetConfiguration.builder().build().url(url));
    }

    SoapCommunicatorImplementation(HttpCommunicationTargetConfiguration targetConfiguration) {
        configuration.setUrl(validator.notEmptyField(targetConfiguration.path(), "path"));
        if (isNotEmpty(targetConfiguration.url())) {
            configuration.setUrl(targetConfiguration.url());
            return;
        }
        configuration.setUrl(validator.notEmptyField(targetConfiguration.scheme(), "scheme") + SCHEME_DELIMITER
                + validator.notEmptyField(targetConfiguration.host(), "url")
                + COLON + validator.notNullField(targetConfiguration.port(), "port"));
    }

    @Override
    public SoapCommunicator client(HttpClient syncClient) {
        configuration.setSyncHttpClient(validator.notEmptyField(syncClient, "syncClient"));
        return this;
    }

    @Override
    public SoapCommunicator operationId(String operationId) {
        configuration.setOperationId(validator.notEmptyField(operationId, "operationId"));
        return this;
    }

    @Override
    public SoapCommunicator requestConfig(RequestConfig requestConfig) {
        RequestConfig config = getOrElse(requestConfig, httpClientModule().getRequestConfig());
        configuration.setRequestConfig(validator.notNullField(config, "requestConfig"));
        return this;
    }

    @Override
    public SoapCommunicator requestMapper(ValueFromModelMapper<?, XmlEntity> requestMapper) {
        configuration.setRequestMapper(validator.notNullField(requestMapper, "requestMapper"));
        return this;
    }

    @Override
    public SoapCommunicator responseMapper(ValueToModelMapper<?, XmlEntity> responseMapper) {
        configuration.setResponseMapper(validator.notNullField(responseMapper, "responseMapper"));
        return this;
    }

    @Override
    public SoapCommunicator envelopeNamespace(String prefix, String namespace) {
        String envelopePrefix = getOrElse(prefix, soapClientModule().getEnvelopePrefix());
        String envelopeNamespace = getOrElse(namespace, soapClientModule().getEnvelopeNamespace());
        configuration.setEnvelopePrefix(validator.notEmptyField(envelopePrefix, "envelopePrefix"));
        configuration.setEnvelopeNamespace(validator.notEmptyField(envelopeNamespace, "envelopeNamespace"));
        return this;
    }

    @Override
    public SoapCommunicator bodyNamespace(String prefix, String namespace) {
        String bodyPrefix = getOrElse(prefix, soapClientModule().getBodyPrefix());
        String bodyNamespace = getOrElse(namespace, soapClientModule().getBodyNamespace());
        configuration.setBodyPrefix(validator.notEmptyField(bodyPrefix, "bodyPrefix"));
        configuration.setBodyNamespace(validator.notEmptyField(bodyNamespace, "bodyNamespace"));
        return this;
    }

    @Override
    public SoapCommunicator operationNamespace(String prefix, String namespace) {
        configuration.setOperationPrefix(validator.notEmptyField(prefix, "operationPrefix"));
        configuration.setOperationNamespace(validator.notEmptyField(namespace, "operationNamespace"));
        return this;
    }

    @Override
    public SoapCommunicator consumes(SoapMimeToContentTypeMapper soapMimeType) {
        configuration.setConsumesMimeType(validator.notNullField(soapMimeType, "consumesMimeType"));
        return this;
    }

    @Override
    public SoapCommunicator produces(SoapMimeToContentTypeMapper soapMimeType) {
        configuration.setProducesMimeType(validator.notNullField(soapMimeType, "producesMimeType"));
        return this;
    }

    @Override
    public SoapCommunicator requestCharset(Charset charset) {
        Charset requestCharset = getOrElse(charset, contextConfiguration().getCharset());
        configuration.setRequestCharset(validator.notNullField(requestCharset, "requestCharset"));
        return this;
    }

    @Override
    public SoapCommunicator requestBodyEncoding(String encoding) {
        configuration.setRequestBodyEncoding(emptyIfNull(encoding));
        return this;
    }

    @Override
    public SoapCommunicator withRequestInterceptor(HttpClientInterceptor interceptor) {
        configuration.getRequestInterceptors().add(validator.notNullField(interceptor, "requestInterceptor"));
        return this;
    }

    @Override
    public SoapCommunicator withResponseInterceptor(HttpClientInterceptor interceptor) {
        configuration.getResponseInterceptors().add(validator.notNullField(interceptor, "responseInterceptor"));
        return this;
    }

    @Override
    public SoapCommunicator version(HttpVersion httpVersion) {
        HttpVersion httpProtocolVersion = getOrElse(httpVersion, httpClientModule().getHttpVersion());
        configuration.setHttpVersion(validator.notEmptyField(httpProtocolVersion, "httpVersion"));
        return this;
    }

    @Override
    public <RequestType, ResponseType> Optional<ResponseType> execute(RequestType request) {
        configuration.setRequest(validator.notNullField(request, "request"));
        validator.validate();
        configuration.validateRequiredFields();
        return SoapCommunicationExecutor.execute(configuration);
    }

    @Override
    public SoapAsynchronousCommunicator client(HttpAsyncClient asyncClient) {
        configuration.setAsyncHttpClient(validator.notNullField(asyncClient, "asyncClient"));
        return this;
    }

    @Override
    public <RequestType, ResponseType> SoapAsynchronousCommunicator responseHandler(HttpCommunicationResponseHandler<RequestType, ResponseType> httpCommunicationResponseHandler) {
        configuration.setResponseHandler(validator.notNullField(httpCommunicationResponseHandler, "httpAsyncClientResponseHandler"));
        return this;
    }

    @Override
    public <RequestType> SoapAsynchronousCommunicator exceptionHandler(HttpCommunicationExceptionHandler<RequestType> httpCommunicationExceptionHandler) {
        configuration.setExceptionHandler(validator.notNullField(httpCommunicationExceptionHandler, "httpAsyncClientExceptionHandler"));
        return this;
    }

    @Override
    public <RequestType> SoapAsynchronousCommunicator cancellationHandler(HttpCommunicationCancellationHandler<RequestType> httpAsyncClientCancellationHandler) {
        configuration.setCancellationHandler(validator.notNullField(httpAsyncClientCancellationHandler, "httpAsyncClientCancellationHandler"));
        return this;
    }

    @Override
    public SoapAsynchronousCommunicator asynchronous() {
        return this;
    }

    @Override
    public <RequestType> void executeAsynchronous(RequestType request) {
        configuration.setRequest(validator.notNullField(request, "request"));
        validator.validate();
        configuration.validateRequiredFields();
        SoapCommunicationExecutor.executeAsync(configuration);
    }
}
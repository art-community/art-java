package ru.adk.http.client.interceptor;

import lombok.Builder;
import lombok.Getter;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import ru.adk.core.constants.InterceptionStrategy;
import ru.adk.http.client.constants.HttpClientExceptionMessages;
import ru.adk.http.client.exception.HttpClientException;
import static java.util.Objects.isNull;
import static ru.adk.http.client.constants.HttpClientExceptionMessages.RESPONSE_INTERCEPTION_IS_NULL;
import static ru.adk.http.client.interceptor.HttpClientRequestInterception.interceptAndContinue;


@Getter
@Builder
public class HttpClientInterceptor {
    private HttpClientRequestInterception requestInterception;
    private HttpClientResponseInterception responseInterception;

    public static HttpClientInterceptor interceptWithHeader(String headerName, String headerValue) {
        return HttpClientInterceptor.builder()
                .requestInterception(interceptAndContinue(request -> request.addHeader(headerName, headerValue)))
                .build();
    }

    public static HttpClientInterceptor interceptRequest(HttpClientRequestInterception interception) {
        if (isNull(interception))
            throw new HttpClientException(HttpClientExceptionMessages.REQUEST_INTERCEPTION_IS_NULL);
        return HttpClientInterceptor.builder()
                .requestInterception(interception)
                .build();
    }

    public static HttpClientInterceptor interceptResponse(HttpClientResponseInterception interception) {
        if (isNull(interception)) throw new HttpClientException(RESPONSE_INTERCEPTION_IS_NULL);
        return HttpClientInterceptor.builder()
                .responseInterception(interception)
                .build();
    }

    public InterceptionStrategy interceptRequest(HttpUriRequest request) {
        try {
            return requestInterception.intercept(request);
        } catch (Exception e) {
            throw new HttpClientException(e);
        }
    }

    public InterceptionStrategy interceptResponse(HttpUriRequest request, HttpResponse response) {
        try {
            return responseInterception.intercept(request, response);
        } catch (Exception e) {
            throw new HttpClientException(e);
        }
    }
}

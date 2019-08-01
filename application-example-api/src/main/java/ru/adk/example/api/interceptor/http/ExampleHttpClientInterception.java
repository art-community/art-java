package ru.adk.example.api.interceptor.http;

import org.apache.http.client.methods.HttpUriRequest;
import ru.adk.core.constants.InterceptionStrategy;
import ru.adk.http.client.interceptor.HttpClientRequestInterception;
import static ru.adk.core.constants.CharConstants.COLON;
import static ru.adk.core.constants.InterceptionStrategy.NEXT_INTERCEPTOR;
import java.util.Base64;

public class ExampleHttpClientInterception implements HttpClientRequestInterception {

    /**
     * Client http interceptor can add some data into request
     * for example can add header for basic authorization
     *
     * @param httpRequest - contains some data of http request
     * @return NEXT_INTERCEPTOR policy for other interceptors
     */

    @Override
    public InterceptionStrategy intercept(HttpUriRequest httpRequest) {
        String login = "login";
        String password = "password";
        String encodedLoginPass = Base64.getEncoder().encodeToString((login + COLON + password).getBytes());

        httpRequest.addHeader("Authorization", "Basic " + encodedLoginPass);
        return NEXT_INTERCEPTOR;
    }
}
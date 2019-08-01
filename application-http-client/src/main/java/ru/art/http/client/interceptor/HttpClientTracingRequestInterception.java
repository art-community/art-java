package ru.art.http.client.interceptor;

import org.apache.http.client.methods.HttpUriRequest;
import ru.art.core.constants.InterceptionStrategy;
import static org.apache.logging.log4j.ThreadContext.get;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.InterceptionStrategy.NEXT_INTERCEPTOR;
import static ru.art.http.client.constants.HttpClientModuleConstants.TRACE_ID_HEADER;
import static ru.art.logging.LoggingModuleConstants.LoggingParameters.TRACE_ID_KEY;

public class HttpClientTracingRequestInterception implements HttpClientRequestInterception {
    @Override
    public InterceptionStrategy intercept(HttpUriRequest request) {
        String traceId = get(TRACE_ID_KEY);
        if (isNotEmpty(traceId)) {
            request.addHeader(TRACE_ID_HEADER, traceId);
        }
        return NEXT_INTERCEPTOR;
    }
}

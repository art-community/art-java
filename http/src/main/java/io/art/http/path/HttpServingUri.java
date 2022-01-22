package io.art.http.path;

import io.art.core.annotation.*;
import io.art.core.exception.*;
import io.art.core.model.*;
import static io.art.core.constants.StringConstants.*;
import java.util.function.*;

@Public
public class HttpServingUri {
    private Strategy strategy = Strategy.AUTOMATIC;
    private String manualUri;
    private Function<ServiceMethodIdentifier, String> transformer;

    public static HttpServingUri manual(String uri) {
        HttpServingUri path = new HttpServingUri();
        path.strategy = Strategy.MANUAL;
        path.manualUri = uri;
        return path;
    }

    public static HttpServingUri byServiceMethod() {
        HttpServingUri path = new HttpServingUri();
        path.strategy = Strategy.AUTOMATIC;
        return path;
    }

    public static HttpServingUri fromServiceMethod(Function<ServiceMethodIdentifier, String> transformer) {
        HttpServingUri path = new HttpServingUri();
        path.strategy = Strategy.TRANSFORMED;
        path.transformer = transformer;
        return path;
    }

    public String make(ServiceMethodIdentifier id) {
        switch (strategy) {
            case AUTOMATIC:
                return SLASH + id.getServiceId() + SLASH + id.getMethodId();
            case MANUAL:
                return manualUri;
            case TRANSFORMED:
                return transformer.apply(id);
        }
        throw new ImpossibleSituationException();
    }

    enum Strategy {
        AUTOMATIC,
        MANUAL,
        TRANSFORMED
    }
}

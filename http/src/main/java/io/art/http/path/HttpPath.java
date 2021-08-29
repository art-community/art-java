package io.art.http.path;

import io.art.core.exception.*;
import io.art.core.model.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.StringExtensions.*;
import static io.art.http.path.HttpPath.Strategy.*;
import java.util.function.*;

public class HttpPath {
    private Strategy strategy = AUTOMATIC;
    private String manualRoute;
    private Function<ServiceMethodIdentifier, String> transformer;

    public static HttpPath manual(String route) {
        HttpPath path = new HttpPath();
        path.strategy = MANUAL;
        path.manualRoute = route;
        return path;
    }

    public static HttpPath byServiceMethod() {
        HttpPath path = new HttpPath();
        path.strategy = AUTOMATIC;
        return path;
    }

    public static HttpPath fromServiceMethod(Function<ServiceMethodIdentifier, String> transformer) {
        HttpPath path = new HttpPath();
        path.strategy = TRANSFORMED;
        path.transformer = transformer;
        return path;
    }

    public String route(ServiceMethodIdentifier id) {
        switch (strategy) {
            case AUTOMATIC:
                return SLASH + decapitalize(id.getServiceId()) + SLASH + id.getMethodId();
            case MANUAL:
                return manualRoute;
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

package io.art.http.strategy;

import io.art.core.exception.*;
import io.art.core.model.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.StringExtensions.*;
import static io.art.http.strategy.RouteByServiceMethodStrategy.Strategy.*;
import java.util.function.*;

public class RouteByServiceMethodStrategy {
    private Strategy strategy = AUTOMATIC;
    private String manualRoute;
    private Function<ServiceMethodIdentifier, String> transformer;

    public static RouteByServiceMethodStrategy manual(String route) {
        RouteByServiceMethodStrategy strategy = new RouteByServiceMethodStrategy();
        strategy.strategy = MANUAL;
        strategy.manualRoute = route;
        return strategy;
    }

    public static RouteByServiceMethodStrategy byServiceMethod() {
        RouteByServiceMethodStrategy strategy = new RouteByServiceMethodStrategy();
        strategy.strategy = AUTOMATIC;
        return strategy;
    }

    public static RouteByServiceMethodStrategy fromServiceMethod(Function<ServiceMethodIdentifier, String> transformer) {
        RouteByServiceMethodStrategy strategy = new RouteByServiceMethodStrategy();
        strategy.strategy = TRANSFORMED;
        strategy.transformer = transformer;
        return strategy;
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

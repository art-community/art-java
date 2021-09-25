package io.art.http.path;

import io.art.core.annotation.*;
import io.art.core.exception.*;
import io.art.core.model.*;
import static io.art.core.constants.StringConstants.*;
import java.util.function.*;

@Public
public class HttpCommunicationUri {
    private Strategy strategy = Strategy.AUTOMATIC;
    private String manualRoute;
    private Function<CommunicatorActionIdentifier, String> transformer;

    public static HttpCommunicationUri manual(String uri) {
        HttpCommunicationUri path = new HttpCommunicationUri();
        path.strategy = Strategy.MANUAL;
        path.manualRoute = uri;
        return path;
    }

    public static HttpCommunicationUri byCommunicatorAction() {
        HttpCommunicationUri path = new HttpCommunicationUri();
        path.strategy = Strategy.AUTOMATIC;
        return path;
    }

    public static HttpCommunicationUri fromCommunicationAction(Function<CommunicatorActionIdentifier, String> transformer) {
        HttpCommunicationUri path = new HttpCommunicationUri();
        path.strategy = Strategy.TRANSFORMED;
        path.transformer = transformer;
        return path;
    }

    public String make(CommunicatorActionIdentifier id) {
        switch (strategy) {
            case AUTOMATIC:
                return SLASH + id.getCommunicatorId().toLowerCase() + SLASH + id.getActionId().toLowerCase();
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

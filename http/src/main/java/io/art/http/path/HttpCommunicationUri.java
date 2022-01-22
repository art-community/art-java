package io.art.http.path;

import io.art.core.annotation.*;
import io.art.core.exception.*;
import io.art.core.model.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.constants.StringConstants.*;
import java.util.function.*;

@Public
@Getter
@Accessors(fluent = true)
public class HttpCommunicationUri {
    private Strategy strategy = Strategy.AUTOMATIC;
    private String manualUri;
    private Function<CommunicatorActionIdentifier, String> transformer;

    public static HttpCommunicationUri manual(String uri) {
        HttpCommunicationUri path = new HttpCommunicationUri();
        path.strategy = Strategy.MANUAL;
        path.manualUri = uri;
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
                return SLASH + id.getCommunicatorId() + SLASH + id.getActionId();
            case MANUAL:
                return manualUri;
            case TRANSFORMED:
                return transformer.apply(id);
        }
        throw new ImpossibleSituationException();
    }

    public enum Strategy {
        AUTOMATIC,
        MANUAL,
        TRANSFORMED
    }

}

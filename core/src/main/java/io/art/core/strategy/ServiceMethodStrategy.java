package io.art.core.strategy;


import io.art.core.exception.*;
import io.art.core.model.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.strategy.ServiceMethodStrategy.Strategy.*;
import java.util.function.*;

public class ServiceMethodStrategy {
    private Strategy strategy = BY_COMMUNICATOR;
    private String manualId;
    private Function<CommunicatorActionIdentifier, ServiceMethodIdentifier> transformer;

    public ServiceMethodStrategy manual(String id) {
        strategy = MANUAL;
        this.manualId = id;
        return this;
    }

    public ServiceMethodStrategy byCommunicator() {
        this.strategy = BY_COMMUNICATOR;
        return this;
    }

    public ServiceMethodStrategy transform(Function<CommunicatorActionIdentifier, ServiceMethodIdentifier> transformer) {
        this.strategy = TRANSFORMED;
        this.transformer = transformer;
        return this;
    }

    public ServiceMethodIdentifier id(CommunicatorActionIdentifier id) {
        switch (strategy) {
            case BY_COMMUNICATOR:
                return serviceMethodId(id.getCommunicatorId(), id.getActionId());
            case MANUAL:
                return serviceMethodId(manualId, id.getActionId());
            case TRANSFORMED:
                return transformer.apply(id);
        }
        throw new ImpossibleSituationException();
    }

    enum Strategy {
        BY_COMMUNICATOR,
        MANUAL,
        TRANSFORMED
    }
}

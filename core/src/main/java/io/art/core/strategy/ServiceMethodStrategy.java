package io.art.core.strategy;


import io.art.core.exception.*;
import io.art.core.model.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.strategy.ServiceMethodStrategy.Strategy.*;
import java.util.function.*;

public class ServiceMethodStrategy {
    private Strategy strategy = AUTOMATIC;
    private String manualId;
    private Function<CommunicatorActionIdentifier, ServiceMethodIdentifier> transformer;

    public static ServiceMethodStrategy manual(String id) {
        ServiceMethodStrategy strategy = new ServiceMethodStrategy();
        strategy.strategy = MANUAL;
        strategy.manualId = id;
        return strategy;
    }

    public static ServiceMethodStrategy manual(Class<?> byClass) {
        return manual(asId(byClass));
    }

    public static ServiceMethodStrategy byCommunicator() {
        ServiceMethodStrategy strategy = new ServiceMethodStrategy();
        strategy.strategy = AUTOMATIC;
        return strategy;
    }

    public static ServiceMethodStrategy fromCommunicator(Function<CommunicatorActionIdentifier, ServiceMethodIdentifier> transformer) {
        ServiceMethodStrategy strategy = new ServiceMethodStrategy();
        strategy.strategy = TRANSFORMED;
        strategy.transformer = transformer;
        return strategy;
    }

    public ServiceMethodIdentifier id(CommunicatorActionIdentifier id) {
        switch (strategy) {
            case AUTOMATIC:
                return serviceMethodId(id.getCommunicatorId(), id.getActionId());
            case MANUAL:
                return serviceMethodId(manualId, id.getActionId());
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

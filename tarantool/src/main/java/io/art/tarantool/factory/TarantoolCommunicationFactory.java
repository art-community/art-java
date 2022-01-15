package io.art.tarantool.factory;

import io.art.core.exception.*;
import io.art.core.model.*;
import io.art.tarantool.communication.*;
import io.art.tarantool.configuration.*;
import lombok.experimental.*;

@UtilityClass
public class TarantoolCommunicationFactory {
    public static TarantoolCommunication createTarantoolCommunication(TarantoolConnectorConfiguration connectorConfiguration, CommunicatorActionIdentifier identifier) {
        throw new ImpossibleSituationException();
    }
}

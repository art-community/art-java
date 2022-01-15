package io.art.tarantool.factory;

import io.art.tarantool.communication.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.connector.*;
import lombok.experimental.*;
import static io.art.tarantool.module.TarantoolModule.*;
import java.util.function.*;

@UtilityClass
public class TarantoolCommunicationFactory {
    public static TarantoolCommunication createTarantoolCommunication(TarantoolConnectorConfiguration connectorConfiguration) {
        String connector = connectorConfiguration.getConnector();
        TarantoolModuleConfiguration moduleConfiguration = tarantoolModule().configuration();
        Supplier<TarantoolConnector> client = () -> new TarantoolConnector(moduleConfiguration.getConnectors().get(connector));
        return new TarantoolCommunication(client, moduleConfiguration);
    }
}

package ru.art.tarantool.storage.vshard.connector;
import io.tarantool.driver.api.TarantoolClient;
import io.tarantool.driver.auth.SimpleTarantoolCredentials;
import io.tarantool.driver.auth.TarantoolCredentials;
import lombok.Getter;
import org.apache.logging.log4j.Logger;
import ru.art.entity.Value;
import ru.art.entity.tuple.PlainTupleReader;
import ru.art.entity.tuple.schema.ValueSchema;
import ru.art.tarantool.exception.TarantoolConnectionException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static ru.art.entity.tuple.PlainTupleReader.readTuple;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.UNABLE_TO_CONNECT_TO_TARANTOOL;
import io.tarantool.driver.*;
import static ru.art.core.caster.Caster.*;

public class VshardCartridgeConnector {
    private final static OutputStream loggerOutputStream = forLogger(loggingModule().getLogger(VshardStandardConnector.class))
            .buildOutputStream();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(VshardStandardConnector.class);

    public TarantoolClient getClient() {
        TarantoolCredentials credentials = new SimpleTarantoolCredentials("username", "password");

        TarantoolClientConfig config = new TarantoolClientConfig.Builder()
                .withCredentials(credentials)
                .withConnectTimeout(1000 * 5)
                .withReadTimeout(1000 * 5)
                .withRequestTimeout(1000 * 5) // 5 seconds, timeout for receiving the server response
                .build();

        getLogger().info("Connecting to tarantool vshard router with cartridge connector...");
        try {
            ClusterTarantoolClient clusterClient = new ClusterTarantoolClient(credentials, "localhost", 3300);
            getLogger().info("Router connected.");
            TarantoolClient client = new ProxyTarantoolClient(clusterClient);
            //testCartridgeClient(client);
            return client;
        } catch(Throwable exception){
            getLogger().error("Some shit happened while connecting. Here`s exception info:\n"
                    + "Class: " + exception.getClass().toString()
                    + "\nMessage: " + exception.getMessage());
        };
        throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, "router", "localhost:3300"));
    }

/*    public void testCall(TarantoolClient client){
        try {
            getLogger().info("Trying to call router function \"vshard.router.info()\"...");

            List<?> response = cast(client.call("vshard.router.info").get());
            getLogger().info("Got response:\n" + response.toString() + "\n");

            ValueSchema schema = ValueSchema.fromTuple(response.get(0));
            getLogger().info("Value schema:\n" + schema.toString() + "\n");

            Value result = readTuple(response, schema);
            getLogger().info("Got result:\n" + result.toString() + "\n");

        } catch (Exception e){
            getLogger().error("Shit happened:\n"
                    + "Class: " + e.getClass()
                    + "\nMessage: " + e.getMessage());
        }
    }
*/
    public void testEval(TarantoolClient client){
        try {
            getLogger().info("Trying to call router function \"crud.get('test', 2)\"...");
            String response;

            response = client.eval("return crud.get('test', 2)").get().toString();
            getLogger().info("Got response:\n" + response);
        } catch (Exception e){
            getLogger().error("Shit happened:\n"
                    + "Class: " + e.getClass()
                    + "\nMessage: " + e.getMessage());
        }
    }

    public void uploadFunction(TarantoolClient client, String function){
        try{
            getLogger().info("Uploading function to router...");
            client.eval(function).get();
        } catch (Exception e) {
            getLogger().error("Shit happened:\n"
                    + "Class: " + e.getClass()
                    + "\nMessage: " + e.getMessage());
        }
    }

}

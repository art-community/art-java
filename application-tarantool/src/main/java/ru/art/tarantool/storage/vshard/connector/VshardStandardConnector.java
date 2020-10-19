package ru.art.tarantool.storage.vshard.connector;

import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.tarantool.TarantoolClient;
import org.tarantool.TarantoolClusterClient;
import org.tarantool.TarantoolClusterClientConfig;
import ru.art.tarantool.exception.TarantoolConnectionException;
import java.io.OutputStream;

import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.UNABLE_TO_CONNECT_TO_TARANTOOL;

public class VshardStandardConnector {
    private final static OutputStream loggerOutputStream = forLogger(loggingModule().getLogger(VshardStandardConnector.class))
            .buildOutputStream();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(VshardStandardConnector.class);

    public static TarantoolClient getClient(String address,TarantoolClusterClientConfig config) {
        try {
            getLogger().info("Connecting to tarantool vshard router [" + address + "]...");
            org.tarantool.TarantoolClient client = new TarantoolClusterClient(config, address);
            getLogger().info("Router connected.");
            return client;
        } catch (Throwable exception){
            getLogger().error("Some shit happened while connecting. Here`s exception info:\n"
                    + "Class: " + exception.getClass().toString()
                    + "\nMessage: " + exception.getMessage());
        }
        throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, "router", "localhost:3300"));
    }

    public static void testCall(TarantoolClient client){
        try {
            getLogger().info("Trying to call router function \"vshard.router.info()\"...");
            String response;
            response = client.syncOps().call("vshard.router.info").toString();
            getLogger().info("Got response:\n" + response);
        } catch (Exception e){
            getLogger().error("Shit happened:\n"
                    + "Class: " + e.getClass()
                    + "\nMessage: " + e.getMessage());
        }
    }

    public static TarantoolClusterClientConfig getDefaultConfig(){
        TarantoolClusterClientConfig config = new TarantoolClusterClientConfig();
        config.username = "username";
        config.password = "password";
        config.initTimeoutMillis = 5 * 1000;
        return config;
    }

}

package io.art.rsocket.configuration.common;

import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import java.io.*;

@Getter
@Builder(toBuilder = true)
public class RsocketSslConfiguration {
    private final File certificate;
    private final File key;
    private final String password;

    public static RsocketSslConfiguration rsocketSsl(ConfigurationSource source) {
        RsocketSslConfigurationBuilder ssl = RsocketSslConfiguration.builder();
        ssl.certificate = source.getFile(CERTIFICATE_KEY);
        ssl.key = source.getFile(KEY_KEY);
        ssl.password = source.getString(PASSWORD_KEY);
        return ssl.build();
    }

    public static RsocketSslConfiguration rsocketSsl(ConfigurationSource source, RsocketSslConfiguration current) {
        RsocketSslConfigurationBuilder ssl = RsocketSslConfiguration.builder();
        ssl.certificate = orElse(source.getFile(CERTIFICATE_KEY), current.certificate);
        ssl.key = orElse(source.getFile(KEY_KEY), current.key);
        ssl.password = orElse(source.getString(PASSWORD_KEY), current.password);
        return ssl.build();
    }
}

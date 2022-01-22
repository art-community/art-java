package io.art.http.configuration;

import io.art.core.annotation.*;
import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import java.io.*;

@Getter
@Public
@Builder(toBuilder = true)
public class HttpSslConfiguration {
    private final File certificate;
    private final File key;
    private final String password;

    public static HttpSslConfiguration httpSsl(ConfigurationSource source) {
        HttpSslConfigurationBuilder ssl = HttpSslConfiguration.builder();
        ssl.certificate = source.getFile(CERTIFICATE_KEY);
        ssl.key = source.getFile(KEY_KEY);
        ssl.password = source.getString(PASSWORD_KEY);
        return ssl.build();
    }

    public static HttpSslConfiguration httpSsl(ConfigurationSource source, HttpSslConfiguration current) {
        HttpSslConfigurationBuilder ssl = HttpSslConfiguration.builder();
        ssl.certificate = orElse(source.getFile(CERTIFICATE_KEY), current.certificate);
        ssl.key = orElse(source.getFile(KEY_KEY), current.key);
        ssl.password = orElse(source.getString(PASSWORD_KEY), current.password);
        return ssl.build();
    }
}

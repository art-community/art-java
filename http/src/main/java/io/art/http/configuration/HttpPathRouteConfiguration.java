package io.art.http.configuration;

import lombok.*;
import java.nio.file.*;

@Getter
@Builder(toBuilder = true)
public class HttpPathRouteConfiguration {
    private final Path path;
}

package io.art.communicator;

import io.art.core.annotation.*;
import static io.art.core.constants.StringConstants.*;

@Public
@FunctionalInterface
public interface ConnectorIdentifier {
    String name();

    default String id() {
        return name().toLowerCase().replaceAll(UNDERSCORE, DASH);
    }
}


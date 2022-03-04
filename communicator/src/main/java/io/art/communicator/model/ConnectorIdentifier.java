package io.art.communicator.model;

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


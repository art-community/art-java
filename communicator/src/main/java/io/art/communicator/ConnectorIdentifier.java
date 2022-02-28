package io.art.communicator;

import static io.art.core.constants.StringConstants.*;

@FunctionalInterface
public interface ConnectorIdentifier {
    String name();

    default String id() {
        return name().toLowerCase().replaceAll(UNDERSCORE, DASH);
    }
}


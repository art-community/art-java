package io.art.communicator.model;

import io.art.communicator.*;
import lombok.*;
import static java.util.Objects.*;

@Getter
@AllArgsConstructor
public class CommunicatorKey {
    private final ConnectorIdentifier connector;
    private final Class<? extends Communicator> type;


    public boolean equals(final Object other) {
        if (other == this) return true;
        if (!(other instanceof CommunicatorKey)) return false;
        final CommunicatorKey otherKey = (CommunicatorKey) other;
        if (!otherKey.canEqual(this)) return false;
        final String currentConnector = this.connector.id();
        final String otherConnector = otherKey.connector.id();
        if (isNull(currentConnector) ? nonNull(otherConnector) : !currentConnector.equals(otherConnector)) return false;
        final Class<? extends Communicator> currentType = this.type;
        final Class<? extends Communicator> otherType = otherKey.type;
        return isNull(currentType) ? !nonNull(otherType) : currentType.equals(otherType);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CommunicatorKey;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object connector = this.getConnector().id();
        result = result * PRIME + (connector == null ? 43 : connector.hashCode());
        final Object type = this.getType();
        result = result * PRIME + (type == null ? 43 : type.hashCode());
        return result;
    }
}

package io.art.core.model;

import lombok.*;
import static io.art.core.constants.StringConstants.EMPTY_STRING;
import static io.art.core.extensions.CollectionExtensions.putIfAbsent;
import static io.art.core.factory.MapFactory.map;
import java.util.*;

@Value
public class CommunicatorActionIdentifier {
    private static final Map<String, CommunicatorActionIdentifier> CACHE = map();

    String communicatorId;
    String actionId;

    public static CommunicatorActionIdentifier communicatorAction(String communicatorId, String actionId) {
        return putIfAbsent(CACHE, EMPTY_STRING + communicatorId + actionId, () -> new CommunicatorActionIdentifier(communicatorId, actionId));
    }

}

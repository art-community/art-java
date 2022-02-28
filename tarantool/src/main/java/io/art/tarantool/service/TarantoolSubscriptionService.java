package io.art.tarantool.service;

import io.art.tarantool.descriptor.*;
import io.art.tarantool.model.*;
import io.art.tarantool.registry.*;
import lombok.experimental.*;
import org.msgpack.value.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static java.util.Objects.*;
import java.util.*;

@UtilityClass
public class TarantoolSubscriptionService {
    public static void publish(ArrayValue bodyValues, TarantoolSubscriptionRegistry subscriptions, TarantoolModelReader reader) {
        Value notification;
        ArrayValue notificationValues;
        if (isNull(notification = bodyValues.get(0)) || !notification.isArrayValue() || (notificationValues = notification.asArrayValue()).size() < 2) {
            return;
        }

        Value serviceId = notificationValues.get(0);
        Value methodId = notificationValues.get(1);

        if (isNull(serviceId) || !serviceId.isStringValue()) {
            return;
        }

        if (isNull(methodId) || !methodId.isStringValue()) {
            return;
        }

        TarantoolSubscription tarantoolSubscription = subscriptions.get(serviceMethodId(serviceId.toString(), methodId.toString()));
        apply(tarantoolSubscription, subscription -> subscription.publish(orNull(() -> notificationValues.get(2), notificationValues.size() == 3), reader));
    }
}

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
    public static void publish(Value payload, TarantoolSubscriptionRegistry subscriptions, TarantoolModelReader reader) {
        if (isNull(payload) || !payload.isMapValue()) {
            return;
        }
        Map<Value, Value> mapValue = payload.asMapValue().map();
        Value bodyData = mapValue.get(IPROTO_BODY_DATA);
        ArrayValue bodyValues;
        if (isNull(bodyData) || !bodyData.isArrayValue() || (bodyValues = bodyData.asArrayValue()).size() != 1) {
            return;
        }
        Value notification;
        ArrayValue notificationValues;
        if (isNull(notification = bodyValues.get(0)) || !notification.isArrayValue() || (notificationValues = notification.asArrayValue()).size() != 3) {
            return;
        }

        Value serviceId = notificationValues.get(0);
        Value methodId = notificationValues.get(1);
        Value request = notificationValues.get(2);

        if (isNull(serviceId) || !serviceId.isStringValue()) {
            return;
        }

        if (isNull(methodId) || !methodId.isStringValue()) {
            return;
        }

        TarantoolSubscription tarantoolSubscription = subscriptions.get(serviceMethodId(serviceId.toString(), methodId.toString()));
        apply(tarantoolSubscription, subscription -> subscription.publish(request, reader));
    }
}

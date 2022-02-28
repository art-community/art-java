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
        Value serviceId = mapValue.get(SERVICE_ID_KEY);
        Value methodId = mapValue.get(METHOD_ID_KEY);
        Value request = mapValue.get(SERVICE_METHOD_REQUEST_KEY);
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

package io.art.tarantool.factory;

import lombok.experimental.*;
import org.msgpack.value.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.AuthenticationMechanism.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

@UtilityClass
public class TarantoolRequestContentFactory {
    public static Value authenticationRequest(String username, byte[] password) {
        Map<IntegerValue, Value> request = map(2);
        request.put(IPROTO_USER_NAME, newString(username));
        request.put(IPROTO_AUTH_DATA, newArray(CHAP_SHA1, newBinary(password)));
        return newMap(request);
    }

    public static Value callRequest(ImmutableStringValue function, ArrayValue arguments) {
        Map<IntegerValue, Value> body = map(2);
        body.put(IPROTO_FUNCTION_NAME, function);
        body.put(IPROTO_TUPLE, arguments);
        return newMap(body);
    }
}

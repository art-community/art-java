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
        Map<IntegerValue, Value> request = map();
        request.put(newInteger(IPROTO_USER_NAME), newString(username));
        request.put(newInteger(IPROTO_AUTH_DATA), newArray(newString(CHAP_SHA1), newBinary(password)));
        return newMap(request);
    }

    public static Value functionRequest(String name, List<Value> arguments) {
        Map<IntegerValue, Value> body = map(2);
        body.put(newInteger(IPROTO_FUNCTION_NAME), newString(name));
        body.put(newInteger(IPROTO_TUPLE), newArray(arguments));
        return newMap(body);
    }
}

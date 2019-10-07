package ru.art.rsocket.factory;

import lombok.experimental.*;
import ru.art.service.model.*;
import static java.util.Objects.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import java.util.function.*;

@UtilityClass
public class RsocketFunctionPredicateFactory {
    public static Predicate<ServiceMethodCommand> byRsocketFunction(String functionId) {
        return command -> nonNull(command) && new ServiceMethodCommand(RSOCKET_FUNCTION_SERVICE, functionId).equals(command);
    }
}

package ru.art.service.factory;

import lombok.experimental.*;
import ru.art.service.model.*;
import static java.util.Objects.nonNull;
import java.util.function.*;

@UtilityClass
public class ServiceCommandPredicateFactory {
    public static Predicate<ServiceMethodCommand> byServiceId(String id) {
        return command -> nonNull(command) && id.equalsIgnoreCase(command.getServiceId());
    }

    public static Predicate<ServiceMethodCommand> byServiceMethodId(String serviceId, String methodId) {
        return command -> nonNull(command) && new ServiceMethodCommand(serviceId, methodId).equals(command);
    }
}

package io.art.http;

import io.art.communicator.*;
import io.art.core.annotation.*;
import io.art.http.state.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.http.module.HttpModule.*;
import java.util.function.*;

@Public
@UtilityClass
public class Http {
    public static <T extends Connector> T httpConnector(Class<T> connectorClass) {
        return httpModule().configuration().getCommunicator().getConnectors().getConnector(connectorClass);
    }

    public <C, M extends MetaClass<C>> HttpLocalState httpState(Class<C> owner, Function<M, MetaMethod<?>> method) {
        return httpModule().state().httpState(owner, method);
    }
}

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
    public static <T extends Portal> T http(Class<T> portalClass) {
        return httpModule().configuration().getCommunicator().getPortals().getPortal(portalClass);
    }

    public <C, M extends MetaClass<C>> HttpLocalState httpState(Class<C> owner, Function<M, MetaMethod<?>> method) {
        return httpModule().state().httpState(owner, method);
    }

    public <C, M extends MetaClass<C>> WsLocalState wsState(Class<C> owner, Function<M, MetaMethod<?>> method) {
        return httpModule().state().wsState(owner, method);
    }
}

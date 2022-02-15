package io.art.http;

import io.art.communicator.*;
import io.art.core.annotation.*;
import io.art.http.communicator.*;
import io.art.http.state.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.http.module.HttpModule.*;

@Public
@UtilityClass
public class Http {
    public static <T extends Portal> T http(Class<T> portalClass) {
        return httpModule().configuration().getCommunicator().getPortals().getPortal(portalClass);
    }

    public static HttpDefaultCommunicator http() {
        return new HttpDefaultCommunicator();
    }

    public <C, M extends MetaClass<C>> HttpLocalState httpState(MetaMethod<M, ?> method) {
        return httpModule().state().httpState(method);
    }

    public <C, M extends MetaClass<C>> WsLocalState wsState(MetaMethod<M, ?> method) {
        return httpModule().state().wsState(method);
    }
}

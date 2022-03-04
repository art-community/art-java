package io.art.http;

import io.art.communicator.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.http.communicator.*;
import io.art.http.state.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.http.module.HttpModule.*;

@Public
@UtilityClass
public class Http {
    public static <T extends Communicator> T http(Class<T> communicatorClass) {
        return http(() -> idByDash(communicatorClass), communicatorClass);
    }

    public static <T extends Communicator> T http(ConnectorIdentifier connector, Class<T> communicatorClass) {
        return httpModule().configuration().getCommunicator().getCommunicators().getCommunicator(connector, communicatorClass).getCommunicator();
    }

    public static HttpDefaultCommunicator http() {
        return new HttpDefaultCommunicator();
    }

    public HttpLocalState httpState(MetaMethod<? extends MetaClass<?>, ?> method) {
        return httpModule().state().httpState(method);
    }

    public WsLocalState wsState(MetaMethod<? extends MetaClass<?>, ?> method) {
        return httpModule().state().wsState(method);
    }
}

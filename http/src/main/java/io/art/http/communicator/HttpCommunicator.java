package io.art.http.communicator;

import io.art.communicator.*;
import io.art.core.annotation.*;
import static io.art.core.caster.Caster.*;
import static io.art.http.communicator.HttpCommunication.*;
import java.util.function.*;

@Public
public interface HttpCommunicator<C extends HttpCommunicator<C>> extends Communicator {
    default C decorate(UnaryOperator<HttpCommunicationDecorator> decorator) {
        decorateHttpCommunication(decorator);
        return cast(this);
    }
}

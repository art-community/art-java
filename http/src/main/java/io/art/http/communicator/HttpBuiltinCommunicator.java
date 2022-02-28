package io.art.http.communicator;

import io.art.communicator.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.http.communicator.HttpCommunication.*;
import java.util.function.*;

public interface HttpBuiltinCommunicator extends Communicator {
    Flux<byte[]> execute(Flux<byte[]> input);

    default HttpBuiltinCommunicator decorate(UnaryOperator<HttpCommunicationDecorator> decorator) {
        decorateHttpCommunication(decorator);
        return cast(this);
    }

}

package io.art.http.portal;

import io.art.communicator.*;
import io.art.http.communicator.*;
import reactor.core.publisher.*;

public interface HttpDefaultPortal extends Portal {
    interface HttpBuiltinCommunicator extends HttpCommunicator<HttpBuiltinCommunicator> {
        Flux<byte[]> execute(Flux<byte[]> input);
    }
}

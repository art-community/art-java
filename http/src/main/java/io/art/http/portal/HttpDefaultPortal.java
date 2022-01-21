package io.art.http.portal;

import io.art.communicator.*;
import io.art.http.communicator.*;
import reactor.core.publisher.*;

public interface HttpDefaultPortal extends Portal {
    interface HttpExecutionCommunicator extends HttpCommunicator<HttpExecutionCommunicator> {
        Flux<byte[]> execute(Flux<byte[]> input);
    }
}

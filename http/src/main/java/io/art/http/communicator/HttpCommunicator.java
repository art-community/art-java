package io.art.http.communicator;

import io.art.communicator.*;
import io.art.core.annotation.*;
import io.art.transport.constants.*;
import io.netty.handler.codec.http.*;
import reactor.netty.http.client.*;
import static io.art.core.caster.Caster.*;
import static io.art.http.communicator.HttpCommunication.*;
import java.util.function.*;

@Public
public interface HttpCommunicator<C extends HttpCommunicator<C>> extends Communicator {
    default C decorate(UnaryOperator<HttpCommunicationDecorator> decorator) {
        decorateHttpCommunication(decorator);
        return cast(this);
    }

    default C get() {
        return decorate(HttpCommunicationDecorator::get);
    }

    default C post() {
        return decorate(HttpCommunicationDecorator::post);
    }

    default C put() {
        return decorate(HttpCommunicationDecorator::put);
    }

    default C patch() {
        return decorate(HttpCommunicationDecorator::patch);
    }

    default C options() {
        return decorate(HttpCommunicationDecorator::options);
    }

    default C head() {
        return decorate(HttpCommunicationDecorator::head);
    }

    default C ws() {
        return decorate(HttpCommunicationDecorator::ws);
    }

    default C pathParameter(String value) {
        return decorate(decorator -> decorator.pathParameter(value));
    }

    default C queryParameter(String name, String value) {
        return decorate(decorator -> decorator.queryParameter(name, value));
    }

    default C headers(UnaryOperator<HttpHeaders> headers) {
        return decorate(decorator -> decorator.headers(headers));
    }

    default C client(UnaryOperator<HttpClient> client) {
        return decorate(decorator -> decorator.client(client));
    }

    default C uri(String uri) {
        return decorate(decorator -> decorator.uri(uri));
    }

    default C uri(UnaryOperator<String> mapper) {
        return decorate(decorator -> decorator.uri(mapper));
    }

    default C input(TransportModuleConstants.DataFormat format) {
        return decorate(decorator -> decorator.input(format));
    }

    default C output(TransportModuleConstants.DataFormat format) {
        return decorate(decorator -> decorator.output(format));
    }
}

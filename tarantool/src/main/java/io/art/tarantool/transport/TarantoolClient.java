package io.art.tarantool.transport;

import io.art.core.exception.*;
import io.art.core.extensions.*;
import io.art.logging.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.exception.*;
import io.netty.buffer.*;
import lombok.*;
import org.msgpack.core.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.*;
import reactor.core.publisher.*;
import reactor.netty.*;
import reactor.netty.tcp.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.extensions.RandomExtensions.*;
import static io.art.tarantool.authenticator.TarantoolAuthenticator.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static io.art.transport.allocator.WriteBufferAllocator.*;
import static io.art.transport.module.TransportModule.*;
import static org.msgpack.value.ValueFactory.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

@RequiredArgsConstructor
public class TarantoolClient {
    private NettyInbound inbound;
    private NettyOutbound outbound;
    private volatile Disposable disposer;
    private final Sinks.One<TarantoolClient> connector = Sinks.one();
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicBoolean authenticated = new AtomicBoolean(false);
    private final TarantoolInstanceConfiguration configuration;

    public Mono<TarantoolClient> connect() {
        disposer = TcpClient.create()
                .host(configuration.getHost())
                .port(configuration.getPort())
                .connect()
                .subscribe(connection -> handle(connection.inbound(), connection.outbound()));
        return connector.asMono();
    }

    public void dispose() {
        apply(disposer, Disposable::dispose);
    }

    public void handle(NettyInbound inbound, NettyOutbound outbound) {
        if (connected.compareAndSet(false, true)) {
            this.inbound = inbound;
            this.outbound = outbound;
            inbound.receive()
                    .doOnError(v -> Logging.logger().info(v.toString()))
                    .subscribe(bytes -> receive(outbound, bytes));
        }
    }

    private void receive(NettyOutbound outbound, ByteBuf bytes) {
        Logging.logger().info("Received: " + NettyBufferExtensions.toString(bytes));
        if (authenticated.compareAndSet(false, true)) {
            outbound.send(Flux.just(write(randomPositiveInt(), IPROTO_AUTH, createAuthenticationRequest(bytes, configuration.getUsername(), configuration.getPassword())))).then().subscribe();
            connector.tryEmitValue(this);
        }
    }

    public void send(Mono<Value> input) {
        if (!connected.get()) {
            throw new ImpossibleSituationException();
        }

        int syncId = randomPositiveInt();

        outbound.send(input.map(value -> write(syncId, IPROTO_CALL, value))).then().subscribe();
    }

    private ByteBuf write(int syncId, int typeId, Value body) {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        try {
            Map<IntegerValue, IntegerValue> header = new HashMap<>();
            header.put(newInteger(IPROTO_REQUEST_TYPE), newInteger(typeId));
            header.put(newInteger(IPROTO_SYNC), newInteger(syncId));
            packer.packValue(newMap(header));
            packer.packValue(body);
            ByteBuf buffer = allocateWriteBuffer(transportModule().configuration());
            long outputSize = packer.getTotalWrittenBytes();
            buffer.capacity((int) (outputSize + MINIMAL_HEADER_SIZE));
            byte[] output = packer.toByteArray();
            packer.clear();
            packer.packLong(outputSize);
            buffer.writeBytes(packer.toByteArray());
            packer.close();
            System.out.println(header);
            System.out.println(body);
            return buffer.writeBytes(output);
        } catch (IOException ioException) {
            throw new TarantoolModuleException(ioException.getMessage());
        }
    }
}

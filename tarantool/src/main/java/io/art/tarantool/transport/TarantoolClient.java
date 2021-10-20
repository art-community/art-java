package io.art.tarantool.transport;

import io.art.tarantool.exception.*;
import io.netty.buffer.*;
import org.msgpack.core.*;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import reactor.netty.*;
import reactor.netty.tcp.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.extensions.RandomExtensions.*;
import static io.art.transport.allocator.WriteBufferAllocator.*;
import static io.art.transport.module.TransportModule.*;
import static org.msgpack.value.ValueFactory.*;
import java.io.*;
import java.util.*;

public class TarantoolClient {
    private static final int VERSION_LENGTH = 64;
    private static final int SALT_LENGTH = 44;
    private static final int GREETING_LENGTH = 128;
    private static final int IPROTO_REQUEST_TYPE = 0x00;
    private static final int IPROTO_SYNC = 0x01;
    private static final int IPROTO_SCHEMA_VERSION = 0x05;
    private NettyInbound inbound;
    private NettyOutbound outbound;

    public void main(String[] args) {
        TcpClient.create()
                .connect()
                .subscribe(connection -> handle(connection.inbound(), connection.outbound()));
    }

    public void handle(NettyInbound inbound, NettyOutbound outbound) {
        this.inbound = inbound;
        this.outbound = outbound;

        // тут где-то авторизация
    }

    @SuppressWarnings(CALLING_SUBSCRIBE_IN_NON_BLOCKING_SCOPE)
    public Mono<ByteBuf> send(Mono<ByteBuf> input) {
        int syncId = randomInt();

        outbound.send(input.map(inputBuffer -> write(syncId, inputBuffer)))
                .then()
                .subscribe();

        return inbound.receive()
                .aggregate()
                .filter(inputId -> inputId.readInt() == syncId)
                .map(b -> /*тут где-то парсинг*/ b);
    }

    public static ByteBuf write(int syncId, ByteBuf inputBuffer) {
        MessageBufferPacker bufferPacker = MessagePack.newDefaultBufferPacker();
        try {
            Map<IntegerValue, IntegerValue> header = new HashMap<>();
            header.put(newInteger(IPROTO_REQUEST_TYPE), newInteger(1));
            header.put(newInteger(IPROTO_SYNC), newInteger(syncId));
            bufferPacker.packValue(newMap(header));
            bufferPacker.addPayload(inputBuffer.array());
        } catch (IOException ioException) {
            throw new TarantoolModuleException(ioException.getMessage());
        }
        return allocateWriteBuffer(transportModule().configuration()).writeBytes(bufferPacker.toMessageBuffer().array());
    }
}

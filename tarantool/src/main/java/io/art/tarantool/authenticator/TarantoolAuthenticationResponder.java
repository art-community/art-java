package io.art.tarantool.authenticator;

import io.art.tarantool.model.transport.*;
import io.netty.buffer.*;
import io.netty.channel.*;
import lombok.*;
import org.msgpack.value.Value;
import static io.art.core.checker.NullityChecker.*;
import static io.art.tarantool.descriptor.TarantoolResponseReader.*;
import java.util.function.*;

@RequiredArgsConstructor
public class TarantoolAuthenticationResponder extends SimpleChannelInboundHandler<ByteBuf> {
    private final BiConsumer<Boolean, String> listener;

    @Override
    protected void channelRead0(ChannelHandlerContext context, ByteBuf input) {
        TarantoolResponse tarantoolResponse = readTarantoolResponse(input);
        listener.accept(!tarantoolResponse.isError(), let(tarantoolResponse.getBody(), Value::toJson));
        context.pipeline().remove(this);
    }
}
